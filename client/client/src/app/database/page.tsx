"use client";

import useGetDatabases from "@/apis/database/useGetDatabases";

export default function DatabasePage() {
  const { data } = useGetDatabases();
  return <div>데이터베이스</div>;
}
