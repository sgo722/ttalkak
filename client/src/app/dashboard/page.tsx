"use client";

import { useEffect, useState } from "react";
import DoughnutChart from "./DoughnutChart";
import useGetProject from "@/apis/project/useGetProject";
import useGetProjects from "@/apis/project/useGetProjects";
import useDashboardLog from "@/apis/project/useDashboardLog";
import { getISODate } from "@/utils/getISODate";
import useAuthStore from "@/store/useAuthStore";
import { GetProjectsParams, Project, Deployment } from "@/types/project";
import { DeploymentLogParams, DeploymentLog } from "@/types/dashboard";

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

  const { data: selectedProjectData, isLoading } = useGetProject(
    selectedProjectId || 0,
    !!selectedProjectId
  );

  const { data: deployLog } = useDashboardLog(
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

  // Project와 Deploy가 선택되면 로그 데이터를 불러옴
  useEffect(() => {
    if (selectedProjectId && selectedDeployId && project) {
      const createdAt = project.createdAt;
      let from = "";
      let to = new Date().toISOString();

      if (selectedDate === "today") {
        from = getISODate();
      } else if (selectedDate === "week") {
        from = getISODate(7);
      } else if (selectedDate === "total" && createdAt) {
        from = createdAt;
      }

      setLogParams({
        from,
        to,
        method: ["GET", "POST", "PUT", "PATCH", "DELETE"],
        status: ["2", "3", "4", "5"],
        deploymentId: selectedDeployId,
        sort: "DESC",
      });
    }
  }, [selectedProjectId, selectedDeployId, selectedDate, project]);

  // 프로젝트 선택
  const handleProjectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const projectId = Number(e.target.value);
    setSelectedProjectId(projectId);
  };

  // Deploy 선택
  const handleDeployChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const deployId = Number(e.target.value);
    setSelectedDeployId(deployId);
  };

  // 날짜 선택 변경
  const handleDateChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedDate(e.target.value);
  };

  // Type 선택 (method 또는 status)
  const handleTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedType(e.target.value);
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

  return (
    <>
      <div>대시보드</div>

      <select onChange={handleProjectChange} value={selectedProjectId ?? ""}>
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

      <select
        onChange={handleDateChange}
        value={selectedDate}
        disabled={!selectedProjectId || !selectedDeployId}
      >
        <option value="today">오늘</option>
        <option value="week">이번 주</option>
        <option value="total">전체</option>
      </select>

      <div className="border">
        <select onChange={handleTypeChange} value={selectedType}>
          <option value="method">Method</option>
          <option value="status">Status</option>
        </select>

        <DoughnutChart title={chartTitle} counts={chartData || {}} />
      </div>
    </>
  );
}
