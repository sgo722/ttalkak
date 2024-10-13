import { useRouter } from "next/navigation";
import { useForm, Controller } from "react-hook-form";
import { CreateDatabaseRequest, DatabaseType } from "@/types/database";
import useAuthStore from "@/store/useAuthStore";
import Tooltip from "@/components/Tooltip";
import Button from "@/components/Button";
import { databaseOptions } from "@/utils/getDatabaseIcons";
import { IoClose } from "react-icons/io5";
import { TbExternalLink } from "react-icons/tb";

interface CreateModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: CreateDatabaseRequest) => void;
}

export default function CreateModal({
  isOpen,
  onClose,
  onSubmit,
}: CreateModalProps) {
  const router = useRouter();

  const { isValidated } = useAuthStore();

  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<CreateDatabaseRequest>({
    defaultValues: {
      name: "",
      type: DatabaseType.MYSQL,
    },
  });

  const handleLinkClick = () => {
    router.push("/mypage");
  };

  const onSubmitForm = (data: CreateDatabaseRequest) => {
    onSubmit(data);
    onClose();
  };

  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-10">
      <div className="bg-white p-10 rounded-lg shadow-xl relative">
        <IoClose
          className="w-6 h-6 absolute top-6 right-6 cursor-pointer"
          onClick={onClose}
        />
        <h2 className="text-4xl font-bold mb-8 text-center">
          데이터베이스 생성
        </h2>
        {!isValidated && (
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
              htmlFor="databaseName"
              className="flex gap-2 items-center block text-sm font-medium text-gray-700 mb-2"
            >
              <div className="bg-black text-white w-5 text-center rounded-[4px]">
                1
              </div>
              데이터베이스명을 입력해주세요
            </label>
            <input
              id="databaseName"
              {...register("name", {
                required: "데이터베이스명은 필수입니다",
                maxLength: {
                  value: 20,
                  message: "데이터베이스명은 20자를 초과할 수 없습니다.",
                },
              })}
              placeholder="데이터베이스명"
              className="w-full p-2 border border-gray-300 rounded"
            />
            {errors.name && (
              <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>
            )}
          </div>

          <div className="mb-4">
            <label className="flex gap-2 items-center block text-sm font-medium text-gray-700 mb-2">
              <div className="bg-black text-white w-5 text-center rounded-[4px]">
                2
              </div>
              데이터베이스 타입을 선택해주세요
            </label>
            <Controller
              name="type"
              control={control}
              rules={{ required: "데이터베이스 타입을 선택해주세요" }}
              render={({ field }) => (
                <div className="grid grid-cols-3 gap-4">
                  {databaseOptions.map((db) => (
                    <div
                      key={db.name}
                      className={`p-4 border rounded-lg cursor-pointer flex flex-col items-center justify-center ${
                        field.value === db.type
                          ? "ring-2 ring-blue-500"
                          : "border-gray-300"
                      }`}
                      onClick={() => field.onChange(db.type)}
                    >
                      <db.icon className="w-12 h-12 mb-2" />
                      <span>{db.name}</span>
                    </div>
                  ))}
                </div>
              )}
            />
            {errors.type && (
              <p className="text-red-500 text-sm mt-1">{errors.type.message}</p>
            )}
          </div>

          <div className="text-center mt-8">
            <Button label="생성" type="submit" primary size="large" />
          </div>
        </form>
      </div>
    </div>
  );
}
