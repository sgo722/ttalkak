import { FaDatabase } from "react-icons/fa";

export default function NoData({ message = "데이터가 없습니다." }) {
  return (
    <div className="flex flex-col items-center justify-center min-h-[calc(100vh-388px)]">
      <FaDatabase className="w-16 h-16 text-gray-400 mb-4" />
      <h2 className="text-xl font-semibold text-gray-700 mb-2">{message}</h2>
    </div>
  );
}
