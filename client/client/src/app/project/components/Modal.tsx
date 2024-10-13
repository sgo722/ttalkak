import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useForm, Controller } from "react-hook-form";
import {
  ProjectFormData,
  CreateProjectRequest,
  PaymentType,
} from "@/types/project";
import { Project } from "@/types/project";
import useAuthStore from "@/store/useAuthStore";
import Tooltip from "@/components/Tooltip";
import Button from "@/components/Button";
import { IoClose } from "react-icons/io5";
import { BiInfinite } from "react-icons/bi";
import { FaCheck } from "react-icons/fa6";
import { FaRegCalendarAlt } from "react-icons/fa";
import { TbExternalLink } from "react-icons/tb";

interface ProjectModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: CreateProjectRequest) => void;
  project?: Project;
  mode: "create" | "edit";
}

export default function Modal({
  isOpen,
  onClose,
  onSubmit,
  project,
  mode,
}: ProjectModalProps) {
  const router = useRouter();

  const { isValidated } = useAuthStore();

  const {
    register,
    handleSubmit,
    control,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm<ProjectFormData>({
    defaultValues: {
      projectName: "",
      domainName: "",
      paymentType: PaymentType.Unlimited,
      expirationDate: new Date().toISOString().split("T")[0],
    },
  });

  const paymentType = watch("paymentType");

  useEffect(() => {
    if (isOpen) {
      if (mode === "edit" && project) {
        const isUnlimited = project.expirationDate === "9999-12-31";
        reset({
          projectName: project.projectName,
          domainName: project.domainName,
          paymentType: isUnlimited
            ? PaymentType.Unlimited
            : PaymentType.FixedTerm,
          expirationDate: isUnlimited
            ? new Date().toISOString().split("T")[0]
            : project.expirationDate,
        });
      } else {
        reset({
          projectName: "",
          domainName: "",
          paymentType: PaymentType.Unlimited,
          expirationDate: new Date().toISOString().split("T")[0],
        });
      }
    }
  }, [isOpen, reset, project, mode]);

  const handleLinkClick = () => {
    router.push("/mypage");
  };

  const onSubmitForm = (data: ProjectFormData) => {
    const formattedDate =
      data.paymentType === PaymentType.Unlimited
        ? "9999-12-31"
        : data.expirationDate;
    onSubmit({
      projectName: data.projectName,
      domainName: data.domainName,
      expirationDate: formattedDate,
    });
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-10">
      <div className="bg-white p-10 rounded-lg shadow-xl relative">
        <IoClose
          className="w-8 h-8 absolute top-8 right-7 cursor-pointer"
          onClick={() => {
            reset();
            onClose();
          }}
        />
        <h2 className="text-4xl font-bold mb-8 text-center">
          프로젝트 {mode === "create" ? "생성" : "수정"}
        </h2>
        {mode === "create" && !isValidated && (
          <div className="flex items-center mb-4">
            <Tooltip
              content={
                "지갑 정보가 등록되지 않았습니다. 서비스를 계속 이용하시려면 마이페이지에서 블록체인 지갑 정보를 등록해 주세요."
              }
              spanClassName="mr-2"
              iconClassName="w-5 h-5 text-red-500"
            />
            <p className="text-red-500 text-sm">
              현재 체험판 모드로 서비스가 제공되고 있어, 15분 후 서버가 자동으로
              종료됩니다.
            </p>
            <TbExternalLink
              onClick={handleLinkClick}
              className="ml-1 text-red-500 cursor-pointer hover:scale-110"
            />
          </div>
        )}
        <form onSubmit={handleSubmit(onSubmitForm)}>
          <div className="mb-4">
            <label
              htmlFor="projectName"
              className="flex gap-2 items-center block text-sm font-medium text-gray-700 mb-2"
            >
              <div className="bg-black text-white w-5 text-center rounded-[4px]">
                1
              </div>
              프로젝트명을 입력해 주세요
            </label>
            <input
              id="projectName"
              {...register("projectName", {
                required: "프로젝트명은 필수입니다",
                maxLength: {
                  value: 20,
                  message: "프로젝트명은 20자를 초과할 수 없습니다.",
                },
              })}
              placeholder="프로젝트명"
              className="w-full p-2 border border-gray-300 rounded"
            />
            {errors.projectName && (
              <p className="text-red-500 text-sm mt-1">
                {errors.projectName.message}
              </p>
            )}
          </div>
          <div className="mb-4">
            <label
              htmlFor="domainName"
              className="flex gap-2 items-center block text-sm font-medium text-gray-700 mb-2"
            >
              <div className="bg-black text-white w-5 text-center rounded-[4px]">
                2
              </div>
              {mode === "create"
                ? "도메인명을 입력해 주세요"
                : "도메인명은 수정 불가능합니다"}
            </label>
            <input
              id="domainName"
              {...register("domainName", {
                required: "도메인명은 필수입니다",
                pattern: {
                  value: /^[a-zA-Z][a-zA-Z]{0,19}$/,
                  message: "올바른 형식이 아닙니다. 영어만 사용 가능합니다.",
                },
                maxLength: {
                  value: 20,
                  message: "도메인명은 20자를 초과할 수 없습니다.",
                },
              })}
              placeholder="도메인명 (영어만 사용가능)"
              className={`w-full p-2 border rounded ${mode === "edit" ? "bg-gray-100 text-gray-500 cursor-not-allowed outline-none" : "border-gray-300"}`}
              readOnly={mode === "edit"}
            />
            {mode === "create" && errors.domainName && (
              <p className="text-red-500 text-sm mt-1">
                {errors.domainName.message}
              </p>
            )}
          </div>

          <div className="mb-4">
            <label className="flex gap-2 items-center block text-sm font-medium text-gray-700 mb-2">
              <div className="bg-black text-white w-5 text-center rounded-[4px]">
                3
              </div>
              결제유형을 선택해 주세요
            </label>
            <div className="flex gap-5">
              <Controller
                name="paymentType"
                control={control}
                render={({ field }) => (
                  <>
                    <div
                      className={`flex-2 p-8 border rounded-lg cursor-pointer ${
                        field.value === PaymentType.FixedTerm
                          ? "ring-2 ring-blue-500 text-black"
                          : "border-gray-300 text-gray-400"
                      } `}
                      onClick={() => field.onChange(PaymentType.FixedTerm)}
                    >
                      <h3 className="font-bold mb-4">기간제</h3>
                      {field.value !== PaymentType.FixedTerm && (
                        <FaRegCalendarAlt className="w-7 h-7" />
                      )}

                      <div className="mb-3">
                        <Controller
                          name="expirationDate"
                          control={control}
                          rules={{
                            required:
                              paymentType === PaymentType.FixedTerm
                                ? "만료일은 필수입니다"
                                : false,
                            validate: (value) =>
                              paymentType === PaymentType.Unlimited ||
                              new Date(value) > new Date() ||
                              "선택한 날짜는 오늘 이후여야 합니다.",
                          }}
                          render={({ field }) => (
                            <input
                              type="date"
                              {...field}
                              className={`w-full border border-gray-300 rounded ${
                                paymentType === PaymentType.Unlimited
                                  ? "hidden"
                                  : ""
                              }`}
                              disabled={paymentType !== PaymentType.FixedTerm}
                            />
                          )}
                        />
                        {errors.expirationDate && (
                          <p className="text-red-500 text-sm mt-1">
                            {errors.expirationDate.message}
                          </p>
                        )}
                      </div>
                      <ul className="text-sm">
                        <li className="flex gap-2">
                          <FaCheck />
                          선택한 날짜까지 프로젝트 이용가능
                        </li>
                        <li className="flex gap-2">
                          <FaCheck />
                          매달 1일 사용량에 따라 요금 청구
                        </li>
                        <li className="flex gap-2">
                          <FaCheck />
                          전자 영수증 메일로 전송
                        </li>
                      </ul>
                    </div>
                    <div
                      className={`flex-2 p-8 border rounded-lg cursor-pointer ${
                        field.value === PaymentType.Unlimited
                          ? "ring-2 ring-blue-500 text-black"
                          : "border-gray-300 text-gray-400"
                      }`}
                      onClick={() => {
                        field.onChange(PaymentType.Unlimited);
                        setValue("expirationDate", "");
                      }}
                    >
                      <h3 className="font-bold mb-3">무기한</h3>
                      <div className="mb-2">
                        <BiInfinite className="w-8 h-8" />
                      </div>
                      <ul className="text-sm">
                        <li className="flex gap-2">
                          <FaCheck />
                          비활성화 전까지 무기한 사용 가능
                        </li>
                        <li className="flex gap-2">
                          <FaCheck />
                          매달 1일 사용량에 따른 요금 청구
                        </li>
                        <li className="flex gap-2">
                          <FaCheck />
                          전자 영수증 메일로 전송
                        </li>
                      </ul>
                    </div>
                  </>
                )}
              />
            </div>
          </div>

          <div className="text-center mt-8">
            <Button
              label={mode === "create" ? "생성" : "수정"}
              type="submit"
              primary
              size="large"
            />
          </div>
        </form>
      </div>
    </div>
  );
}
