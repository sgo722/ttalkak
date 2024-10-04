"use client";

import { useParams } from "next/navigation";
import useGetDatabase from "@/apis/database/useGetDatabase";
export default function DatabaseDetailPage() {
  const params = useParams();
  const databaseId = params.id;

  const { data } = useGetDatabase(Number(databaseId));
  return <div>데이터베이스 상세페이지</div>;
}
