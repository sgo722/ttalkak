import { FaSearch } from "react-icons/fa";

export default function NoSearchResult({ message = "검색 결과가 없습니다." }) {
  return (
    <div className="flex flex-col items-center justify-center min-h-[calc(100vh-388px)]">
      <FaSearch className="w-16 h-16 text-gray-400 mb-4" />
      <h2 className="text-xl font-semibold text-gray-700 text-center">
        {message}
      </h2>
    </div>
  );
}
