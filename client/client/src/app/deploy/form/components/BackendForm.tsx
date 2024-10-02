"use client";

import { useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { useForm, Controller, useFieldArray, useWatch } from "react-hook-form";
import {
  DatabaseType,
  ServiceType,
  Framework,
  DockerfileCreateRequest,
} from "@/types/deploy";
import useDeployStore from "@/store/useDeployStore";
import useCreateDeploy from "@/apis/deploy/useCreateDeploy";
import useCreateWebhook from "@/apis/webhook/useCreateWebhook";
import { LuMinusCircle } from "react-icons/lu";
import { MdAdd } from "react-icons/md";

interface DatabaseForm {
  databaseType: DatabaseType;
  databaseName?: string;
  databasePort: string;
  databaseUsername?: string;
  databasePassword?: string;
}

interface FormData {
  javaVersion: string;
  port: string;
  rootDir: string;
  useDatabase: boolean;
  databases: DatabaseForm[];
  envVars: { key: string; value: string }[];
}

export default function BackendForm() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const projectId = searchParams.get("projectId");
  const typeParam = searchParams.get("type");

  const {
    githubRepositoryRequest,
    versionRequest,
    dockerfileCreateRequest,
    reset: resetDeployStore,
  } = useDeployStore();

  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
  } = useForm<FormData>({
    defaultValues: {
      javaVersion: "",
      port: "8080",
      useDatabase: false,
      databases: [{ databaseType: DatabaseType.MYSQL, databasePort: "" }],
      envVars: [],
    },
  });

  const useDatabase = watch("useDatabase");

  const {
    fields: envFields,
    append: appendEnv,
    remove: removeEnv,
  } = useFieldArray({
    control,
    name: "envVars",
  });

  const {
    fields: dbFields,
    append: appendDb,
    remove: removeDb,
  } = useFieldArray({
    control,
    name: "databases",
  });

  const databaseTypes = useWatch({
    control,
    name: "databases",
    defaultValue: [],
  });

  useEffect(() => {
    if (!useDatabase) {
      setValue("databases", []);
    } else if (dbFields.length === 0) {
      appendDb({ databaseType: DatabaseType.MYSQL, databasePort: "" });
    }
  }, [useDatabase, setValue, dbFields.length, appendDb]);

  const { mutate: createDeploy } = useCreateDeploy();
  const { mutate: createWebhook } = useCreateWebhook();

  const isValidDatabaseName = (
    name: string,
    type: DatabaseType
  ): true | string => {
    //postgresql은 최대 63바이트, 다른 db는 64바이트
    const maxLength = type === DatabaseType.POSTGRESQL ? 63 : 64;

    // 빈 문자열이거나 최대 길이를 초과하면 유효하지 않음
    if (name.length === 0 || name.length > maxLength) {
      return "데이터베이스 이름을 입력해주세요.";
    }

    switch (type) {
      case DatabaseType.MYSQL:
      case DatabaseType.MARIADB:
        // MySQL과 MariaDB: 영문자, 숫자, 언더스코어, 달러 기호 허용, 숫자로 시작 불가
        if (!/^[a-zA-Z_$]/.test(name)) {
          return "데이터베이스 이름은 영문자, 언더스코어(_), 달러 기호($)로 시작해야 합니다.";
        }
        if (/[^a-zA-Z0-9_$]/.test(name)) {
          return "데이터베이스 이름은 영문자, 숫자, _, $ 기호만 사용 가능합니다.";
        }
        break;
      case DatabaseType.POSTGRESQL:
        // PostgreSQL: 소문자, 숫자, 언더스코어 허용, 숫자로 시작 불가, 'pg_' 시작 불가
        if (!/^[a-z_]/.test(name)) {
          return "데이터베이스 이름은 소문자나 언더스코어(_)로 시작해야 합니다.";
        }
        if (/[^a-z0-9_]/.test(name)) {
          return "데이터베이스 이름은 소문자, 숫자, 언더스코어(_)만 사용 가능합니다.";
        }
        if (/^pg_/.test(name)) {
          return "데이터베이스 이름은 'pg_'로 시작할 수 없습니다.";
        }
        break;
      case DatabaseType.MONGODB:
        // MongoDB: 영문자, 숫자, 언더스코어, 하이픈 허용, 특정 예약어 사용 불가,
        // system.으로 시작하는 이름 사용 불가
        if (name.startsWith("system.")) {
          return "데이터베이스 이름은 'system.'으로 시작할 수 없습니다.";
        }
        if (!/^[a-zA-Z0-9_-]+$/.test(name)) {
          return "데이터베이스 이름은 영문자, 숫자, 언더스코어(_), 하이픈(-)만 사용 가능합니다.";
        }
        if (["admin", "local", "config"].includes(name)) {
          return "데이터베이스 이름으로 'admin', 'local', 'config'는 사용할 수 없습니다.";
        }
        break;
    }

    return true;
  };

  const onSubmit = (data: FormData) => {
    const databaseCreateRequests = data.useDatabase
      ? data.databases.map((db) => ({
          databaseName: db.databaseType,
          name: db.databaseType === "REDIS" ? "" : db.databaseName || "",
          databasePort: Number(db.databasePort),
          username:
            db.databaseType === "REDIS" ? "" : db.databaseUsername || "",
          password: db.databasePassword || "",
        }))
      : null;

    const updatedDockerfileCreateRequest: DockerfileCreateRequest = {
      exist: dockerfileCreateRequest?.exist ?? true,
      ...dockerfileCreateRequest,
      languageVersion: data.javaVersion,
    };

    createDeploy(
      {
        projectId: Number(projectId),
        serviceType: serviceType as ServiceType,
        hostingPort: Number(data.port),
        githubRepositoryRequest,
        versionRequest,
        databaseCreateRequests,
        dockerfileCreateRequest: updatedDockerfileCreateRequest,
        envs: data.envVars,
        framework: Framework.SPRINGBOOT,
      },
      {
        onSuccess: (responseData) => {
          // 웹훅 생성
          createWebhook({
            owner: githubRepositoryRequest.repositoryOwner,
            repo: githubRepositoryRequest.repositoryName,
            webhookUrl: responseData.webhookUrl,
          });
          resetDeployStore();
          router.push(`/projects/${projectId}`);
        },
      }
    );
  };

  const serviceType: ServiceType | null =
    typeParam === "FRONTEND" || typeParam === "BACKEND"
      ? (typeParam as ServiceType)
      : null;

  return (
    <>
      <form
        id="form"
        onSubmit={handleSubmit(onSubmit)}
        className="w-full mx-auto p-6 bg-white rounded-lg border"
      >
        <div className="space-y-6">
          <Controller
            name="javaVersion"
            control={control}
            rules={{
              required: "자바 버전을 입력해주세요.",
              pattern: {
                value: /^[0-9]+$/,
                message: "숫자만 입력 가능합니다.",
              },
            }}
            render={({ field }) => (
              <div>
                <label
                  htmlFor="javaVersion"
                  className="block text-md font-semibold text-gray-700 mb-1"
                >
                  자바 버전
                </label>
                <input
                  {...field}
                  id="javaVersion"
                  type="text"
                  placeholder="예 : 17"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                {errors.javaVersion && (
                  <p className="text-red-500 text-sm mt-2">
                    {errors.javaVersion.message}
                  </p>
                )}
              </div>
            )}
          ></Controller>

          <div>
            <label className="block text-md font-semibold text-gray-700 mb-1">
              백엔드 프레임워크
            </label>
            <div className="w-full p-3 border border-gray-300 rounded-md bg-gray-100">
              SpringBoot
            </div>
          </div>

          <Controller
            name="port"
            control={control}
            rules={{
              required: "포트번호를 입력해주세요.",
              pattern: {
                value: /^[0-9]+$/,
                message: "포트 번호는 숫자만 입력 가능합니다.",
              },
            }}
            render={({ field }) => (
              <div>
                <label
                  htmlFor="port"
                  className="block text-md font-semibold text-gray-700 mb-1"
                >
                  포트번호
                </label>
                <input
                  {...field}
                  id="port"
                  type="number"
                  min="0"
                  max="65535"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                {errors.port && (
                  <p className="text-red-500 text-sm mt-2">
                    {errors.port.message}
                  </p>
                )}
              </div>
            )}
          />
          <div>
            <label
              htmlFor="branch"
              className="block text-md font-semibold text-gray-700 mb-1"
            >
              브랜치
            </label>
            <input
              id="branch"
              type="text"
              value={githubRepositoryRequest.branch}
              readOnly
              className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 focus:outline-none"
            />
          </div>

          <div>
            <label
              htmlFor="rootDir"
              className="block text-md font-semibold text-gray-700 mb-1"
            >
              루트 디렉토리
            </label>
            <input
              id="rootDir"
              type="text"
              value={githubRepositoryRequest.rootDirectory}
              readOnly
              className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 focus:outline-none"
            />
          </div>

          <div className="mt-8">
            <h3 className="block text-md font-semibold text-gray-700 mb-1">
              환경변수
            </h3>
            <div className="space-y-3">
              {envFields.map((field, index) => (
                <div key={field.id} className="flex items-center space-x-2">
                  <Controller
                    name={`envVars.${index}.key`}
                    control={control}
                    render={({ field }) => (
                      <input
                        {...field}
                        placeholder="Key"
                        className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    )}
                  />
                  <Controller
                    name={`envVars.${index}.value`}
                    control={control}
                    render={({ field }) => (
                      <input
                        {...field}
                        placeholder="Value"
                        className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    )}
                  />
                  <button
                    type="button"
                    onClick={() => removeEnv(index)}
                    className="p-2.5 rounded-md text-gray-400 border border-gray-300 hover:text-gray-600"
                  >
                    <LuMinusCircle className="w-5 h-5" />
                  </button>
                </div>
              ))}
            </div>
            <button
              type="button"
              onClick={() => appendEnv({ key: "", value: "" })}
              className="mt-3 flex items-center px-4 py-2 text-sm bg-gray-200 rounded-md hover:bg-blue-200"
            >
              <MdAdd className="mr-2" /> 추가
            </button>
          </div>

          <Controller
            name="useDatabase"
            control={control}
            render={({ field: { onChange, value, ...field } }) => (
              <div className="flex items-center gap-2">
                <input
                  {...field}
                  type="checkbox"
                  checked={value}
                  onChange={(e) => onChange(e.target.checked)}
                  className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
                <label
                  htmlFor="useDatabase"
                  className="block text-md font-semibold text-gray-700 mb-1"
                >
                  데이터베이스 사용여부
                </label>
              </div>
            )}
          />

          {useDatabase && (
            <div className="space-y-4">
              {dbFields.map((field, index) => (
                <div
                  key={field.id}
                  className="p-4 border border-gray-300 rounded-md"
                >
                  <div className="flex justify-between items-center mb-3">
                    <h3 className="text-lg font-semibold">
                      Database {index + 1}
                    </h3>
                    {index > 0 && (
                      <button
                        type="button"
                        onClick={() => removeDb(index)}
                        className="py-2 px-4 border border-red-500 text-red-500 rounded-md focus:outline-none"
                      >
                        삭제
                      </button>
                    )}
                  </div>
                  <div className="space-y-4">
                    <Controller
                      name={`databases.${index}.databaseType`}
                      control={control}
                      render={({ field }) => (
                        <div>
                          <label
                            htmlFor={`databases.${index}.databaseType`}
                            className="block text-md font-semibold text-gray-700 mb-1"
                          >
                            데이터베이스 종류
                          </label>
                          <select
                            {...field}
                            id={`databases.${index}.databaseType`}
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                          >
                            <option value={DatabaseType.MYSQL}>MySQL</option>
                            <option value={DatabaseType.MARIADB}>
                              MariaDB
                            </option>
                            <option value={DatabaseType.MONGODB}>
                              MongoDB
                            </option>
                            <option value={DatabaseType.POSTGRESQL}>
                              PostgreSQL
                            </option>
                            <option value={DatabaseType.REDIS}>Redis</option>
                          </select>
                        </div>
                      )}
                    />

                    {databaseTypes[index]?.databaseType !== "REDIS" && (
                      <Controller
                        name={`databases.${index}.databaseName`}
                        control={control}
                        rules={{
                          required: "데이터베이스 이름 입력해주세요.",
                          validate: (value) => {
                            const dbType = databaseTypes[index]?.databaseType;
                            if (!dbType) {
                              return "데이터베이스 타입이 유효하지 않습니다.";
                            }
                            if (typeof value !== "string") {
                              return "데이터베이스 이름은 문자열이어야 합니다.";
                            }
                            const result = isValidDatabaseName(value, dbType);
                            return result === true ? true : result;
                          },
                        }}
                        render={({ field }) => (
                          <div>
                            <label
                              htmlFor={`databases.${index}.databaseName`}
                              className="block text-md font-semibold text-gray-700 mb-1"
                            >
                              데이터베이스 이름
                            </label>
                            <input
                              {...field}
                              id={`databases.${index}.databaseUsername`}
                              type="text"
                              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            />
                            {errors.databases?.[index]?.databaseName && (
                              <p className="text-red-500 text-sm mt-2">
                                {errors.databases[index].databaseName?.message}
                              </p>
                            )}
                          </div>
                        )}
                      />
                    )}

                    {databaseTypes[index]?.databaseType !== "REDIS" && (
                      <Controller
                        name={`databases.${index}.databaseUsername`}
                        control={control}
                        rules={{
                          required: "데이터베이스 아이디를 입력해주세요.",
                          pattern: {
                            value: /^[A-Za-z0-9]+$/,
                            message: "영어와 숫자만 입력 가능합니다.",
                          },
                        }}
                        render={({ field }) => (
                          <div>
                            <label
                              htmlFor={`databases.${index}.databaseUsername`}
                              className="block text-md font-semibold text-gray-700 mb-1"
                            >
                              데이터베이스 아이디
                            </label>
                            <input
                              {...field}
                              id={`databases.${index}.databaseUsername`}
                              type="text"
                              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            />
                            {errors.databases?.[index]?.databaseUsername && (
                              <p className="text-red-500 text-sm mt-2">
                                {
                                  errors.databases[index].databaseUsername
                                    ?.message
                                }
                              </p>
                            )}
                          </div>
                        )}
                      />
                    )}

                    <Controller
                      name={`databases.${index}.databasePassword`}
                      control={control}
                      rules={{
                        required: "데이터베이스 비밀번호를 입력해주세요.",
                      }}
                      render={({ field }) => (
                        <div>
                          <label
                            htmlFor={`databases.${index}.databasePassword`}
                            className="block text-md font-semibold text-gray-700 mb-1"
                          >
                            데이터베이스 비밀번호
                          </label>
                          <input
                            {...field}
                            id={`databases.${index}.databasePassword`}
                            type="password"
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                          />
                          {errors.databases?.[index]?.databasePassword && (
                            <p className="text-red-500 text-sm mt-2">
                              {
                                errors.databases[index].databasePassword
                                  ?.message
                              }
                            </p>
                          )}
                        </div>
                      )}
                    />

                    <Controller
                      name={`databases.${index}.databasePort`}
                      control={control}
                      rules={{
                        required: "포트번호를 입력해주세요.",
                        pattern: {
                          value: /^\d+$/,
                          message: "숫자만 입력가능합니다.",
                        },
                      }}
                      render={({ field, fieldState: { error } }) => (
                        <div>
                          <label
                            htmlFor={`databases.${index}.databasePort`}
                            className="block text-md font-semibold text-gray-700 mb-1"
                          >
                            데이터베이스 포트번호
                          </label>
                          <input
                            {...field}
                            id={`databases.${index}.databasePort`}
                            type="number"
                            min="0"
                            max="65535"
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                          />
                          {error && (
                            <p className="text-red-500 text-sm mt-2">
                              {error.message}
                            </p>
                          )}
                        </div>
                      )}
                    />
                  </div>
                </div>
              ))}
              <button
                type="button"
                onClick={() =>
                  appendDb({
                    databaseType: DatabaseType.MYSQL,
                    databasePort: "",
                  })
                }
                className="mt-4 py-2 px-4 border text-black rounded-md focus:outline-none"
              >
                추가
              </button>
            </div>
          )}
        </div>
      </form>
      <div className="flex mt-8 justify-end">
        <button
          type="submit"
          form="form"
          className="py-2 px-4 bg-black border rounded-md text-md font-medium text-white focus:outline-none"
        >
          다음
        </button>
      </div>
    </>
  );
}
