"use client";

import { useEffect, useState } from "react";
import DoughnutChart from "./components/DoughnutChart";
import useGetProjectToLog from "@/apis/project/useGetProjectToLog";
import useGetProjects from "@/apis/project/useGetProjects";
import useGetLog from "@/apis/project/useGetLog";
import { getISODate, getWeekStartDate } from "@/utils/getDate";
import useAuthStore from "@/store/useAuthStore";
import { GetProjectsParams, Project, Deployment } from "@/types/project";
import { DeploymentLogParams, DeploymentLog } from "@/types/dashboard";
import { IoRefresh } from "react-icons/io5";

export default function CallbackPage() {
  const { userInfo } = useAuthStore();
  const [projects, setProjects] = useState<Project[]>([]);
  const [project, setProject] = useState<Project | null>(null);
  const [selectedProjectId, setSelectedProjectId] = useState<number | null>(
    null
  );
  const [selectedDeployId, setSelectedDeployId] = useState<number | null>(null);
  const [selectedDate, setSelectedDate] = useState<string>("today");
  const [selectedType, setSelectedType] = useState<string>("method"); // method or status 선택
  const [logParams, setLogParams] = useState<DeploymentLogParams | null>(null);
  const [logData, setLogData] = useState<DeploymentLog | null>(null);

  const params: GetProjectsParams = {
    page: 0,
    size: 999,
    sort: "createdAt",
    direction: "DESC",
    userId: userInfo?.userId ?? 1,
    searchKeyword: "",
  };

  const { data: projectData } = useGetProjects(params);

  const { data: selectedProjectData, isLoading } = useGetProjectToLog(
    selectedProjectId || 0,
    !!selectedProjectId
  );

  const { data: deployLog } = useGetLog(
    logParams as DeploymentLogParams,
    !!logParams && !!selectedDeployId && !!selectedDate
  );

  // Project 선택
  useEffect(() => {
    if (projectData) {
      setProjects(projectData.content);
    }
  }, [projectData]);

  // Deploy 선택
  useEffect(() => {
    if (selectedProjectData) {
      setProject(selectedProjectData);
    } else {
      setProject(null);
    }
  }, [selectedProjectData]);

  // LogData 저장
  useEffect(() => {
    if (deployLog) {
      console.log("로그 데이터", deployLog);
      setLogData(deployLog);
    }
  }, [deployLog]);

  // 로그 파라미터 설정 함수
  const updateLogParams = () => {
    if (selectedProjectId && selectedDeployId && project) {
      const createdAt = project.createdAt.slice(0, 23) + "Z";
      let from = "";
      let to = new Date();

      to.setTime(to.getTime() + 9 * 60 * 60 * 1000); // 한국 시간으로 변환

      const toISOString = to.toISOString();

      if (selectedDate === "today") {
        from = getISODate(); // 오늘 자정부터 현재까지
      } else if (selectedDate === "week") {
        from = getWeekStartDate(); // 이번 주 시작(일요일)부터
      } else if (selectedDate === "total" && createdAt) {
        from = createdAt; // 프로젝트 생성일부터
      }

      setLogParams({
        from,
        to: toISOString,
        method: ["GET", "POST", "PUT", "PATCH", "DELETE"],
        status: ["2", "3", "4", "5"],
        deploymentId: selectedDeployId,
        sort: "DESC",
      });
    }
  };

  useEffect(() => {
    updateLogParams();
  }, [selectedProjectId, selectedDeployId, selectedDate, project]);

  // Project 선택
  const handleProjectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const projectId = Number(e.target.value);
    setSelectedProjectId(projectId);
  };

  // Deploy 선택
  const handleDeployChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const deployId = Number(e.target.value);
    setSelectedDeployId(deployId);
  };

  // 날짜 선택
  const handleDateChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedDate(e.target.value);
  };

  // Type 선택 (method 또는 status)
  const handleTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedType(e.target.value);
  };

  // 새로고침
  const handleRefresh = () => {
    updateLogParams();
  };

  const chartTitle =
    selectedType === "method" ? "Method Counts" : "Status Counts";
  const chartData =
    selectedType === "method"
      ? Object.entries(logData?.methodCounts || {}).reduce(
          (acc, [key, value]) => ({ ...acc, [key]: Number(value) }),
          {} as Record<string, number>
        )
      : Object.entries(logData?.statusCounts || {}).reduce(
          (acc, [key, value]) => ({ ...acc, [key]: Number(value) }),
          {} as Record<string, number>
        );

  const flexClass = "flex items-center justify-between";

  return (
    <>
      <div>대시보드</div>
      <div className={flexClass}>
        <div className={flexClass}>
          <select
            onChange={handleProjectChange}
            value={selectedProjectId ?? ""}
          >
            <option value="">프로젝트</option>
            {projects.map((project: Project) => (
              <option key={project.id} value={project.id}>
                {project.projectName}
              </option>
            ))}
          </select>

          <select
            onChange={handleDeployChange}
            value={selectedDeployId ?? ""}
            disabled={!selectedProjectId || isLoading}
          >
            <option value="">배포 유형</option>
            {project &&
              project.deployments &&
              project.deployments.map((deployment: Deployment) => (
                <option
                  key={deployment.deploymentId}
                  value={deployment.deploymentId}
                >
                  {deployment.serviceType}
                </option>
              ))}
          </select>
        </div>
        <div className={flexClass}>
          <select
            onChange={handleDateChange}
            value={selectedDate}
            disabled={!selectedProjectId || !selectedDeployId}
          >
            <option value="today">오늘</option>
            <option value="week">이번 주</option>
            <option value="total">전체</option>
          </select>

          <IoRefresh onClick={handleRefresh} className="cursor-pointer" />
        </div>
      </div>

      <div className="border">
        <select onChange={handleTypeChange} value={selectedType}>
          <option value="method">Method</option>
          <option value="status">Status</option>
        </select>

        <DoughnutChart
          title={chartTitle}
          counts={chartData || {}}
          logs={logData?.content || []}
        />
      </div>
    </>
  );
}
