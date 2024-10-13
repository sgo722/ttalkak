"use client";

import { Suspense, useState } from "react";
import { GetProjectsRequest, CreateProjectRequest } from "@/types/project";
import ProjectList from "@/app/project/components/ProjectList";
import ProjectListLoading from "@/app/project/components/ProjectListLoading";
import Modal from "@/app/project/components/Modal";
import useCreateProject from "@/apis/project/useCreateProject";
import useDebounce from "@/hooks/useDebounce";
import { IoIosSearch } from "react-icons/io";
import { FaSort } from "react-icons/fa";

export default function ProjectsPage() {
  const { mutate: createProject } = useCreateProject();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [direction, setDirection] = useState("desc");
  const debouncedSearchKeyword = useDebounce(searchKeyword, 500);

  const params: Omit<GetProjectsRequest, "page"> = {
    size: 9,
    sort: "createdAt",
    direction: direction.toUpperCase(),
    searchKeyword: debouncedSearchKeyword,
  };

  const handleCreateProject = (data: CreateProjectRequest) => {
    createProject(data, {
      onSuccess: () => {
        setIsModalOpen(false);
      },
    });
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <div className="relative flex-grow mr-4">
          <input
            className="pl-10 pr-4 py-2 border rounded-lg w-full"
            placeholder="프로젝트를 검색해주세요"
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
        <h1 className="text-2xl font-bold">프로젝트</h1>
        <button
          onClick={() => setIsModalOpen(true)}
          className="border border rounded-lg px-4 py-2"
        >
          생성
        </button>
      </div>
      <Suspense fallback={<ProjectListLoading />}>
        <ProjectList initialParams={params} />
      </Suspense>

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleCreateProject}
        mode="create"
      />
    </div>
  );
}
