import { ErrorBoundaryFallBackProps } from "@/types/errorType";
import Button from "@/components/Button";
import { IoClose } from "react-icons/io5";

export default function DetailError({
  error,
  resetErrorBoundary,
  onClose,
}: ErrorBoundaryFallBackProps) {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-16 rounded-lg shadow-xl relative">
        <IoClose
          className="w-6 h-6 absolute top-6 right-6 cursor-pointer"
          onClick={onClose}
        />
        <h2 className="text-2xl mb-4 text-center">
          데이터베이스 정보를 불러올 수 없습니다
        </h2>
        <p className="mb-4 text-red-500 text-center">{error.message}</p>
        <div className="text-center">
          <Button
            label="다시 시도"
            onClick={resetErrorBoundary}
            type="button"
            primary
            size="large"
          />
        </div>
      </div>
    </div>
  );
}
