"use client";

import { useState } from "react";
import DatabaseModal from "@/app/database/components/DatabaseModal";
import useGetDatabases from "@/apis/database/useGetDatabases";
import useCreateDatabase from "@/apis/database/useCreateDatabase";
import { CreateDatabaseRequest } from "@/types/database";

export default function DatabasePage() {
  const { data } = useGetDatabases();
  const { mutate: createDatabase } = useCreateDatabase();

  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleCreateDatabase = (data: CreateDatabaseRequest) => {
    createDatabase(data, {
      onSuccess: () => {
        setIsModalOpen(false);
      },
    });
  };
  return (
    <>
      <div>데이터베이스</div>
      <button
        onClick={() => setIsModalOpen(true)}
        className="border border rounded-lg px-4 py-2"
      >
        생성
      </button>
      <DatabaseModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleCreateDatabase}
      />
    </>
  );
}
