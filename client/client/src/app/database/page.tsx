"use client";

import { Suspense } from "react";
import { ErrorBoundary } from "react-error-boundary";
import { useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { CreateDatabaseRequest, GetDatabasesParams } from "@/types/database";
import DatabaseList from "@/app/database/components/DatabaseList";
import DatabaseListLoading from "@/app/database/components/DatabaseListLoading";
import CreateModal from "@/app/database/components/CreateModal";
import DetailModal from "@/app/database/components/DetailModal";
import DetailLoading from "@/app/database/components/DetailLoading";
import DetailError from "@/app/database/components/DetailError";
import useCreateDatabase from "@/apis/database/useCreateDatabase";
import useDeleteDatabase from "@/apis/database/useDeleteDatabase";
import useDebounce from "@/hooks/useDebounce";
import { IoIosSearch } from "react-icons/io";
import { FaSort } from "react-icons/fa";

export default function DatabasePage() {
  const queryClient = useQueryClient();
  const [searchKeyword, setSearchKeyword] = useState("");
  const debouncedSearchKeyword = useDebounce(searchKeyword, 500);
  const [direction, setDirection] = useState("desc");
  const [selectedDatabaseId, setSelectedDatabaseId] = useState<number | null>(
    null
  );

  const { mutate: createDatabase } = useCreateDatabase();
  const { mutate: deleteDatabase } = useDeleteDatabase();

  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [detailModalOpen, setDetailModalOpen] = useState(false);

  const params: Omit<GetDatabasesParams, "page"> = {
    size: 9,
    sort: "createdAt",
    direction: direction.toUpperCase(),
    searchKeyword: debouncedSearchKeyword,
  };

  const handleCreateDatabase = (data: CreateDatabaseRequest) => {
    createDatabase(data, {
      onSuccess: () => {
        setCreateModalOpen(false);
      },
    });
  };

  const handleDeleteDatabase = () => {
    if (!selectedDatabaseId) return;
    deleteDatabase(selectedDatabaseId, {
      onSuccess: () => {
        setDetailModalOpen(false);
      },
    });
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <div className="relative flex-grow mr-4">
          <input
            className="pl-10 pr-4 py-2 border rounded-lg w-full"
            placeholder="데이터베이스를 검색해주세요"
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
          />
          <IoIosSearch className="absolute left-3 top-2.5 text-gray-400 text-xl" />
        </div>
        <div className="relative">
          <select
            className="appearance-none bg-white border rounded-lg px-4 py-2 pr-8 text-gray-700"
            value={direction}
            onChange={(e) => setDirection(e.target.value)}
          >
            <option value="desc">최신순</option>
            <option value="asc">오래된순</option>
          </select>
          <FaSort className="absolute right-3 top-3 text-gray-400" />
        </div>
      </div>
      <div className="flex justify-between mb-3">
        <h1 className="text-2xl font-bold">데이터베이스</h1>
        <button
          onClick={() => setCreateModalOpen(true)}
          className="border border rounded-lg px-4 py-2"
        >
          생성
        </button>
      </div>

      <Suspense fallback={<DatabaseListLoading />}>
        <DatabaseList
          initialParams={params}
          onDatabaseClick={(id) => {
            setSelectedDatabaseId(id);
            setDetailModalOpen(true);
          }}
        />
      </Suspense>

      <CreateModal
        isOpen={createModalOpen}
        onClose={() => setCreateModalOpen(false)}
        onSubmit={handleCreateDatabase}
      />
      {detailModalOpen && (
        <ErrorBoundary
          FallbackComponent={({ error, resetErrorBoundary }) => (
            <DetailError
              error={error}
              resetErrorBoundary={resetErrorBoundary}
              onClose={() => setDetailModalOpen(false)}
            />
          )}
          onReset={() => {
            queryClient.invalidateQueries({
              queryKey: ["database", selectedDatabaseId],
            });
          }}
        >
          <Suspense fallback={<DetailLoading />}>
            <DetailModal
              isOpen={detailModalOpen}
              onClose={() => setDetailModalOpen(false)}
              onDelete={handleDeleteDatabase}
              databaseId={selectedDatabaseId}
            />
          </Suspense>
        </ErrorBoundary>
      )}
    </div>
  );
}
