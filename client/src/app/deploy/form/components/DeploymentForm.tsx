"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useForm, useFieldArray, Controller } from "react-hook-form";
import {
  ServiceType,
  Framework,
  DockerfileCreateRequest,
} from "@/types/deploy";
import Tooltip from "@/components/Tooltip";
import useDeployStore from "@/store/useDeployStore";
import useThrottle from "@/hooks/useThrottle";
import useCreateDeploy from "@/apis/deploy/useCreateDeploy";
import useCreateWebhook from "@/apis/webhook/useCreateWebhook";
import { LuMinusCircle } from "react-icons/lu";
import { MdAdd } from "react-icons/md";
import { useEffect } from "react";

const TOOLTIPS = {
  FRONTEND_FRAMEWORK: "프론트엔드는 React와 Next.js만 지원합니다.",
  BACKEND_FRAMEWORK: "백엔드는 SpringBoot만 지원합니다.",
  LANGUAGE_VERSION: {
    FRONTEND: "프로젝트에서 사용한 Node.js 버전을 입력해주세요.",
    BACKEND: "프로젝트에서 사용한 Java 버전을 입력해주세요.",
  },
  PORT: "애플리케이션에서 사용할 포트 번호를 입력해주세요.",
  BRANCH: "배포할 레포지토리의 브랜치명 입니다.",
  ROOT_DIR: "프로젝트의 루트 디렉토리 경로입니다.",
  ENV_VARS: "애플리케이션에 필요한 환경 변수를 입력해주세요.",
};

interface FormData {
  serviceType: ServiceType;
  framework?: Framework;
  languageVersion: string;
  port: number;
  envVars: { key: string; value: string }[];
}

export default function DeploymentForm() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const projectId = searchParams.get("projectId");
  const typeParam = searchParams.get("type") as ServiceType;

  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
  } = useForm<FormData>({
    defaultValues: {
      serviceType: typeParam,
      framework:
        typeParam === ServiceType.FRONTEND ? Framework.REACT : undefined,
      languageVersion: "",
      port: typeParam === ServiceType.FRONTEND ? 80 : 8080,
      envVars: [],
    },
  });

  const serviceType = watch("serviceType");

  const { fields, append, remove } = useFieldArray({
    control,
    name: "envVars",
  });

  const { mutate: createDeploy } = useCreateDeploy();
  const { mutate: createWebhook } = useCreateWebhook();

  const {
    githubRepositoryRequest,
    versionRequest,
    dockerfileCreateRequest,
    favicon,
    reset: resetDelpoyStore,
  } = useDeployStore();

  useEffect(() => {
    setValue("port", serviceType === ServiceType.FRONTEND ? 80 : 8080);
  }, [serviceType, setValue]);

  const onSubmit = useThrottle((data: FormData) => {
    const isNotEmpty = (val: string) => Boolean(val.trim());
    const filteredEnvVars = data.envVars.filter(
      ({ key, value }) => isNotEmpty(key) && isNotEmpty(value)
    );

    const updatedDockerfileCreateRequest: DockerfileCreateRequest = {
      exist: dockerfileCreateRequest?.exist ?? true,
      ...dockerfileCreateRequest,
      languageVersion: data.languageVersion,
    };

    createDeploy(
      {
        projectId: Number(projectId),
        serviceType: serviceType as ServiceType,
        hostingPort: Number(data.port),
        githubRepositoryRequest,
        versionRequest,
        dockerfileCreateRequest: updatedDockerfileCreateRequest,
        envs: filteredEnvVars,
        framework: (data.serviceType === ServiceType.FRONTEND
          ? data.framework
          : Framework.SPRINGBOOT) as Framework,
        favicon: favicon,
      },
      {
        onSuccess: (responseData) => {
          // 웹훅 생성
          createWebhook({
            owner: githubRepositoryRequest.repositoryOwner,
            repo: githubRepositoryRequest.repositoryName,
            webhookUrl: responseData.webhookUrl,
          });
          resetDelpoyStore();
          router.push(`/project/${projectId}`);
        },
      }
    );
  }, 4000);

  return (
    <>
      <form
        id="form"
        onSubmit={handleSubmit(onSubmit)}
        className="w-full mx-auto p-6 bg-white rounded-lg border"
      >
        <div className="space-y-6">
          {serviceType === ServiceType.FRONTEND && (
            <Controller
              name="framework"
              control={control}
              render={({ field }) => (
                <div>
                  <label
                    htmlFor="framework"
                    className="flex items-center text-md font-semibold text-gray-700 mb-1"
                  >
                    프레임워크
                    <Tooltip
                      content={TOOLTIPS.FRONTEND_FRAMEWORK}
                      spanClassName="ml-1"
                    />
                  </label>
                  <select
                    {...field}
                    id="framework"
                    className="w-full p-3 border border-gray-300 rounded-md focus:outline-none"
                  >
                    <option value={Framework.REACT}>React.js</option>
                    <option value={Framework.NEXTJS}>Next.js</option>
                    <option value={Framework.VUEJS}>Vue.js</option>
                  </select>
                </div>
              )}
            />
          )}
          {serviceType === ServiceType.BACKEND && (
            <div>
              <label className="flex items-center text-md font-semibold text-gray-700 mb-1">
                백엔드 프레임워크
                <Tooltip
                  content={TOOLTIPS.BACKEND_FRAMEWORK}
                  spanClassName="ml-1"
                />
              </label>
              <div className="w-full p-3 border border-gray-300 rounded-md bg-gray-100">
                SpringBoot
              </div>
            </div>
          )}

          <Controller
            name="languageVersion"
            control={control}
            rules={{
              required: `${serviceType === ServiceType.FRONTEND ? "Node.js" : "Java"} 버전을 입력해주세요.`,
              validate: (value) => {
                if (serviceType === ServiceType.FRONTEND) {
                  // Node.js 버전 유효성 검사
                  const nodeVersionPattern = /^(1[4-9]|2[0-2])$/;
                  if (!nodeVersionPattern.test(value)) {
                    return "지원되는 Node.js 버전은 14~22 입니다.";
                  }
                  return true;
                } else {
                  // Java 버전 유효성 검사
                  const validJavaVersions = [8, 11, 17];
                  const parsedValue = parseInt(value, 10);
                  if (
                    isNaN(parsedValue) ||
                    !validJavaVersions.includes(parsedValue) ||
                    parsedValue.toString() !== value
                  ) {
                    return "지원되는 Java 버전은 8, 11, 17 입니다.";
                  }
                  return true;
                }
              },
            }}
            render={({ field }) => (
              <div>
                <label
                  htmlFor="languageVersion"
                  className="flex items-center text-md font-semibold text-gray-700 mb-1"
                >
                  {serviceType === ServiceType.FRONTEND ? "Node.js" : "Java"}{" "}
                  버전
                  <Tooltip
                    content={
                      serviceType === ServiceType.FRONTEND
                        ? TOOLTIPS.LANGUAGE_VERSION.FRONTEND
                        : TOOLTIPS.LANGUAGE_VERSION.BACKEND
                    }
                    spanClassName="ml-1"
                  />
                </label>
                <input
                  {...field}
                  id="languageVersion"
                  type="text"
                  placeholder={
                    serviceType === ServiceType.FRONTEND ? "예: 20" : "예: 17"
                  }
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                {errors.languageVersion && (
                  <p className="text-red-500 text-sm mt-2">
                    {errors.languageVersion.message}
                  </p>
                )}
              </div>
            )}
          />

          <Controller
            name="port"
            control={control}
            rules={{
              required: "포트번호를 입력해주세요.",
              pattern: {
                value: /^[0-9]+$/,
                message: "포트 번호는 숫자만 입력 가능합니다.",
              },
              validate: (value) =>
                (value >= 0 && value <= 65535) ||
                "포트 번호는 0에서 65535 사이의 값이어야 합니다.",
            }}
            render={({ field }) => (
              <div>
                <label
                  htmlFor="port"
                  className="flex items-center text-md font-semibold text-gray-700 mb-1"
                >
                  포트번호
                  <Tooltip content={TOOLTIPS.PORT} spanClassName="ml-1" />
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
              className="flex items-center text-md font-semibold text-gray-700 mb-1"
            >
              브랜치
              <Tooltip content={TOOLTIPS.BRANCH} spanClassName="ml-1" />
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
              className="flex items-center text-md font-semibold text-gray-700 mb-1"
            >
              루트 디렉토리
              <Tooltip content={TOOLTIPS.ROOT_DIR} spanClassName="ml-1" />
            </label>
            <input
              id="rootDir"
              type="text"
              value={githubRepositoryRequest.rootDirectory}
              readOnly
              className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 focus:outline-none"
            />
          </div>
        </div>

        <div className="mt-8">
          <h3 className="flex items-center text-md font-semibold text-gray-700 mb-1">
            환경변수
            <Tooltip content={TOOLTIPS.ENV_VARS} spanClassName="ml-1" />
          </h3>
          <div className="space-y-3">
            {fields.map((field, index) => (
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
                  onClick={() => remove(index)}
                  className="p-2.5 rounded-md text-gray-400 border border-gray-300 hover:text-gray-600"
                >
                  <LuMinusCircle className="w-5 h-5" />
                </button>
              </div>
            ))}
          </div>
          <button
            type="button"
            onClick={() => append({ key: "", value: "" })}
            className="mt-3 flex items-center px-4 py-2 text-sm bg-gray-200 rounded-md hover:bg-blue-200"
          >
            <MdAdd className="mr-2" /> 추가
          </button>
        </div>
      </form>
      <div className="flex mt-8 justify-end">
        <button
          type="submit"
          form="form"
          className="py-2 px-4 border rounded-md text-md font-medium text-black focus:outline-none"
        >
          다음
        </button>
      </div>
    </>
  );
}
