import { useState } from "react";
import {
  GetDatabasesParams,
  GetDatabasesContentResponse,
} from "@/types/database";
import NoData from "@/components/NoData";
import NoSearchResult from "@/components/NoSearchResult";
import useGetDatabases from "@/apis/database/useGetDatabases";
import { getDatabaseIcon, getDatabaseName } from "@/utils/getDatabaseIcons";

interface DatabaseListProps {
  initialParams: Omit<GetDatabasesParams, "page">;
  onDatabaseClick: (id: number) => void;
}

export default function DatabaseList({
  initialParams,
  onDatabaseClick,
}: DatabaseListProps) {
  const [currentPage, setCurrentPage] = useState(0);
  const params: GetDatabasesParams = { ...initialParams, page: currentPage };
  const { data } = useGetDatabases(params);

  if (!data || data.content.length === 0) {
    return initialParams.searchKeyword ? (
      <NoSearchResult />
    ) : (
      <NoData message={"등록된 데이터베이스가 없습니다."} />
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {data?.content.map((database: GetDatabasesContentResponse) => {
          const Icon = getDatabaseIcon(database.type);
          return (
            <div
              key={database.id}
              className="border rounded-lg p-6 cursor-pointer"
              onClick={() => onDatabaseClick(database.id)}
            >
              <div className="flex items-center mb-2">
                <Icon className="w-10 h-10 mr-3 text-gray-600" />
                <div>
                  <h2 className="font-semibold">{database.name}</h2>
                  <p className="text-sm text-gray-500">
                    {getDatabaseName(database.type)}
                  </p>
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {data.totalPages > 0 && (
        <div className="flex justify-center mt-8">
          <nav className="inline-flex rounded-md gap-2">
            {data.totalPages > 1 && (
              <button
                onClick={() => setCurrentPage((prev) => prev - 1)}
                className="px-3 py-2 rounded-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                disabled={currentPage === 0}
              >
                이전
              </button>
            )}
            {Array.from({ length: data.totalPages }, (_, i) => i + 1).map(
              (page) => (
                <button
                  key={page}
                  onClick={() => setCurrentPage(page - 1)}
                  className={`px-3 py-2 border border-gray-300 rounded-md bg-white text-sm font-medium ${
                    page === currentPage + 1
                      ? "text-black"
                      : "text-gray-400 hover:bg-gray-50"
                  }`}
                >
                  {page}
                </button>
              )
            )}
            {data.totalPages > 1 && (
              <button
                onClick={() => setCurrentPage((prev) => prev + 1)}
                className="px-3 py-2 rounded-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                disabled={currentPage === (data?.totalPages ?? 1) - 1}
              >
                다음
              </button>
            )}
          </nav>
        </div>
      )}
    </>
  );
}
