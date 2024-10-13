import { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { GetProjectsRequest, Project } from "@/types/project";
import NoData from "@/components/NoData";
import NoSearchResult from "@/components/NoSearchResult";
import useGetProjects from "@/apis/project/useGetProjects";
import { getRelativeTime } from "@/utils/getRelativeTime";

interface ProjectListProps {
  initialParams: Omit<GetProjectsRequest, "page">;
}

export default function ProjectList({ initialParams }: ProjectListProps) {
  const [currentPage, setCurrentPage] = useState(0);
  const params: GetProjectsRequest = { ...initialParams, page: currentPage };
  const { data } = useGetProjects(params);

  if (!data || data.content.length === 0) {
    return initialParams.searchKeyword ? (
      <NoSearchResult />
    ) : (
      <NoData message={"등록된 프로젝트가 없습니다."} />
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {data?.content.map((project: Project) => (
          <Link
            href={`/project/${project.id}`}
            key={project.id}
            className="border rounded-lg p-6"
          >
            <div className="flex items-center mb-2 cursor-pointer">
              <Image
                src={project.favicon || "/favicon.png"}
                alt={`${project.projectName} favicon`}
                width={40}
                height={40}
                className="w-10 h-10 rounded-full mr-3 object-cover"
                onError={(e) => {
                  // 이미지 로드 실패 시 기본 이미지로 대체
                  const target = e.target as HTMLImageElement;
                  target.onerror = null; // 무한 루프 방지
                  target.src = "/favicon.png";
                }}
              />
              <div>
                <h2 className="font-semibold">{project.projectName}</h2>
                <p className="text-sm text-gray-500">{project.domainName}</p>
              </div>
            </div>
            <div className="flex justify-end items-center mt-6">
              <span className="text-sm text-gray-500">
                {getRelativeTime(project.createdAt)}
              </span>
            </div>
          </Link>
        ))}
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
