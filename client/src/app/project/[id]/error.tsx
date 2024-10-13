"use client";

import { useEffect } from "react";
import { ErrorFallBackProps } from "@/types/errorType";
import { BiErrorCircle } from "react-icons/bi";

export default function GetProjectErrorFallBack({
  error,
  reset,
}: ErrorFallBackProps) {
  useEffect(() => {
    console.error(error);
  }, [error]);

  return (
    <div className="flex flex-col items-center justify-center min-h-[calc(100vh-388px)]">
      <BiErrorCircle className="w-16 h-16 text-gray-400 mb-4" />
      <h2 className="text-xl font-semibold text-gray-700 mb-2">
        프로젝트를 불러올 수 없습니다
      </h2>
      <button
        className="mt-4 px-6 py-2 bg-black text-white font-semibold rounded-md shadow-md transition duration-300 ease-in-out transform hover:scale-105"
        onClick={() => reset()}
      >
        다시 시도
      </button>
    </div>
  );
}
