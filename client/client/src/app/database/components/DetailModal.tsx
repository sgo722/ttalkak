import Tooltip from "@/components/Tooltip";
import Button from "@/components/Button";
import { DeployStatus } from "@/types/deploy";
import useGetDatabase from "@/apis/database/useGetDatabase";
import useStatusColor from "@/hooks/useStatusColor";
import { getStatusTooptip } from "@/utils/getStatusTooltip";
import { IoClose, IoEye, IoEyeOff } from "react-icons/io5";
import { useState } from "react";

interface DetailModalProps {
  isOpen: boolean;
  onClose: () => void;
  onDelete: () => void;
  databaseId: number | null;
}

interface DbInfoField {
  key: "type" | "username" | "password" | "port" | "dbName" | "host";
  label: string;
  tooltip: string;
  value?: string;
  isPassword?: boolean;
}

const dbInfoFields: DbInfoField[] = [
  {
    key: "type",
    label: "DB 타입",
    tooltip: "데이터베이스의 종류 (예: MySQL, PostgreSQL)",
  },
  {
    key: "dbName",
    label: "DB 이름",
    tooltip: "데이터베이스 접속에 필요한 데이터베이스명",
  },
  {
    key: "username",
    label: "DB 아이디",
    tooltip: "데이터베이스 접속에 필요한 사용자 아이디",
  },
  {
    key: "password",
    label: "DB 비밀번호",
    tooltip: "데이터베이스 접속에 필요한 비밀번호",
    isPassword: true,
  },
  {
    key: "port",
    label: "DB 포트번호",
    tooltip: "데이터베이스 서버에 접속하기 위한 네트워크 포트",
  },
  {
    key: "host",
    label: "DB 호스트",
    tooltip: "데이터베이스 서버의 호스트 주소",
    value: "database.ttalkak.com",
  },
];

export default function DetailModal({
  isOpen,
  onClose,
  onDelete,
  databaseId,
}: DetailModalProps) {
  const { data } = useGetDatabase(Number(databaseId));
  const [showPassword, setShowPassword] = useState(false);
  const statusColor = useStatusColor(data?.status as DeployStatus);

  const onDeleteDatabase = () => {
    onDelete();
  };

  const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
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
              <div className="flex items-center mb-4">
                <div className={`w-3 h-3 rounded-full ${statusColor}`} />
                <div className="text-md ml-2">{data.status}</div>
                <Tooltip
                  content={getStatusTooptip(data.statusMessage, "데이터베이스")}
                />
              </div>
            )}
            <div className="mb-4">
              <table className="w-full">
                <tbody>
                  {dbInfoFields.map((item, index) => (
                    <tr key={index} className="border-b last:border-b-0">
                      <td className="py-3 text-gray-500 w-1/3">
                        <div className="flex items-center">
                          {item.label}
                          <Tooltip content={item.tooltip} />
                        </div>
                      </td>
                      <td className="py-3">
                        {item.isPassword ? (
                          <div className="flex items-center justify-between">
                            <span>
                              {showPassword ? data[item.key] : "••••••••"}
                            </span>
                            <button
                              onClick={togglePasswordVisibility}
                              className="focus:outline-none"
                            >
                              {showPassword ? (
                                <IoEyeOff className="w-5 h-5" />
                              ) : (
                                <IoEye className="w-5 h-5" />
                              )}
                            </button>
                          </div>
                        ) : (
                          item.value || data[item.key]
                        )}
                      </td>
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
