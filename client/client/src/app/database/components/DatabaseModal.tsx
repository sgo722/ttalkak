import { useForm, Controller } from "react-hook-form";
import Button from "@/components/Button";
import { CreateDatabaseRequest, DatabaseType } from "@/types/database";
import { databaseOptions } from "@/utils/getDatabaseIcons";
import { IoClose } from "react-icons/io5";

interface DatabaseModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: CreateDatabaseRequest) => void;
}

export default function DatabaseModal({
  isOpen,
  onClose,
  onSubmit,
}: DatabaseModalProps) {
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

  const onSubmitForm = (data: CreateDatabaseRequest) => {
    onSubmit(data);
    onClose();
  };

  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white p-16 rounded-lg shadow-xl relative">
        <IoClose
          className="w-6 h-6 absolute top-6 right-6 cursor-pointer"
          onClick={onClose}
        />
        <h2 className="text-4xl font-bold mb-8 text-center">
          데이터베이스 생성
        </h2>
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
