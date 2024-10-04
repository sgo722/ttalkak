import Button from "@/components/Button";
import { DeployStatus } from "@/types/deploy";
import useGetDatabase from "@/apis/database/useGetDatabase";
import useStatusColor from "@/hooks/useStatusColor";
import { IoClose } from "react-icons/io5";

interface DetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  onDelete: () => void;
  databaseId: number | null;
}

export default function DetailModal({
  isOpen,
  onClose,
  onDelete,
  databaseId,
}: DetailModalProps) {
  const { data } = useGetDatabase(Number(databaseId));

  const statusColor = useStatusColor(data?.status as DeployStatus);

  const onDeleteDatabase = () => {
    onDelete();
  };

  const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  if (!isOpen) return null;

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      onClick={handleOverlayClick}
    >
      <div
        className="bg-white p-16 rounded-lg shadow-xl relative"
        onClick={(e) => e.stopPropagation()}
      >
        <IoClose
          className="w-6 h-6 absolute top-6 right-6 cursor-pointer"
          onClick={onClose}
        />
        {data && (
          <>
            <h2 className="text-4xl mb-6 text-center">{data.name}</h2>
            {data.status && (
              <div className="flex items-center gap-2 mb-4">
                <div className={`w-3 h-3 rounded-full ${statusColor}`} />
                <div className="text-md">{data.status}</div>
              </div>
            )}
            <div className="mb-4">
              <table className="w-full">
                <tbody>
                  {[
                    { label: "DB 타입", value: data.type },
                    { label: "DB 아이디", value: data.name },
                    { label: "DB 비밀번호", value: data.password },
                    { label: "DB 포트번호", value: data.port },
                  ].map((item, index) => (
                    <tr key={index} className="border-b last:border-b-0">
                      <td className="py-3 text-gray-500 w-1/3">{item.label}</td>
                      <td className="py-3">{item.value}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        )}
        <div className="flex justify-center p-4 gap-4">
          <div className="text-center mt-8">
            <Button
              label="확인"
              onClick={onClose}
              type="button"
              primary
              size="large"
            />
          </div>
          <div className="text-center mt-8">
            <Button
              label="삭제"
              onClick={onDeleteDatabase}
              type="button"
              size="large"
            />
          </div>
        </div>
      </div>
    </div>
  );
}
