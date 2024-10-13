"use client";

import { useEffect, useState } from "react";
import DoughnutChart from "./components/DoughnutChart";
import useGetProjectToLog from "@/apis/project/useGetProjectToLog";
import useGetProjectsToLog from "@/apis/project/useGetProjectsToLog";
import useGetHistogram from "@/apis/deploy/useGetHistogram";
import useGetLog from "@/apis/project/useGetLog";
import {
  getNowDate,
  getStartDate,
  formatTimestamp,
  toISOWithTimezone,
} from "@/utils/getDate";
import useAuthStore from "@/store/useAuthStore";
import { GetProjectsRequest, Project, Deployment } from "@/types/project";
import {
  HistogramParams,
  DeploymentLogParams,
  DeploymentLog,
} from "@/types/dashboard";
import { IoRefresh } from "react-icons/io5";
import Monitoring from "./components/Monitoring";
import HistogramChart from "./components/HistogramChart";

export default function CallbackPage() {
  const { userInfo } = useAuthStore();
  const [selectedProjectId, setSelectedProjectId] = useState<number | null>(
    null
  );
  const [selectedDeployId, setSelectedDeployId] = useState<number | null>(null);
  const [selectedDate, setSelectedDate] = useState<string>("today");
  const [selectedType, setSelectedType] = useState<string>("method");
  const [fromDate, setFromDate] = useState<string>(getStartDate());
  const [toDate, setToDate] = useState<string>(getNowDate());
  const [logData, setLogData] = useState<DeploymentLog | null>(null);
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [hasMoreLogs, setHasMoreLogs] = useState<boolean>(true);
  const [selectedMethod, setSelectedMethod] = useState<string[]>([
    "GET",
    "POST",
    "PUT",
    "PATCH",
    "DELETE",
  ]);
  const [selectedStatus, setSelectedStatus] = useState<string[]>([
    "2",
    "3",
    "4",
    "5",
  ]);

  const projectsParams: GetProjectsRequest = {
    page: 0,
    size: 999,
    sort: "createdAt",
    direction: "DESC",
    userId: userInfo?.userId ?? 1,
    searchKeyword: "",
  };

  const histogramParams: HistogramParams = {
    from: toISOWithTimezone(fromDate),
    to: toISOWithTimezone(toDate),
    deploymentId: selectedDeployId || 0,
  };

  const logParams: DeploymentLogParams = {
    from: toISOWithTimezone(fromDate),
    to: toISOWithTimezone(toDate),
    method: selectedMethod,
    status: selectedStatus,
    deploymentId: selectedDeployId || 0,
    sort: "DESC",
    page: currentPage,
  };

  const { data: projects } = useGetProjectsToLog(projectsParams);
  const { data: project, isLoading } = useGetProjectToLog(
    selectedProjectId || 0,
    !!selectedProjectId
  );
  const { data: { histograms, intervalMinute } = {} } = useGetHistogram(
    histogramParams as HistogramParams,
    !!histogramParams && !!selectedDeployId
  );
  const { data: deployLog } = useGetLog(
    logParams as DeploymentLogParams,
    !!logParams && !!selectedDeployId
  );

  // LogData 저장 및 추가 로드 시 데이터 병합
  useEffect(() => {
    if (selectedDeployId && deployLog && logParams) {
      setLogData((prevLogData) => {
        const updatedContent =
          logParams.page === 0
            ? deployLog.content // 새로고침일 경우 덮어쓰기
            : [...(prevLogData?.content || []), ...deployLog.content]; // 페이지가 0이 아닐 경우 데이터 병합

        if (deployLog.content.length < 50) {
          setHasMoreLogs(false); // 로그가 50개 미만일 경우 더 이상 로드하지 않음
        }

        return { ...deployLog, content: updatedContent };
      });
    }
  }, [deployLog]);

  const resetData = () => {
    setCurrentPage(0);
    setHasMoreLogs(true);
  };

  // Project 선택
  const handleProjectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedProjectId(Number(e.target.value));
    setSelectedDeployId(null);
    resetData();
  };

  // Deploy 선택
  const handleDeployChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedDeployId(Number(e.target.value));
    resetData();
  };

  // 날짜 선택
  const handleDateChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const selected = e.target.value;
    setSelectedDate(selected);
    setSelectedStatus(["2", "3", "4", "5"]);
    setSelectedMethod(["GET", "POST", "PUT", "PATCH", "DELETE"]);

    if (selected === "today") {
      setFromDate(getStartDate());
      setToDate(getNowDate());
    } else if (selected === "week") {
      setFromDate(getStartDate("week"));
      setToDate(getNowDate());
    } else if (selected === "total") {
      if (project) {
        setFromDate(formatTimestamp(project.createdAt));
      }
      setToDate(getNowDate());
    }

    resetData();
  };

  // Type 선택 (method 또는 status)
  const handleTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedType(e.target.value);

    if (e.target.value === "status") {
      setSelectedMethod(["GET", "POST", "PUT", "PATCH", "DELETE"]);
    } else if (e.target.value === "method") {
      setSelectedStatus(["2", "3", "4", "5"]);
    }
  };

  // 무한 스크롤을 위한 추가 로그 요청
  const fetchMoreLogs = () => {
    if (hasMoreLogs) {
      const nextPage = currentPage + 1;
      setCurrentPage(nextPage);
    }
  };

  // 새로고침
  const handleRefresh = () => {
    setSelectedStatus(["2", "3", "4", "5"]);
    setSelectedMethod(["GET", "POST", "PUT", "PATCH", "DELETE"]);
    setToDate(getNowDate());
    resetData();
  };

  // Status 토글
  const handleStatusToggle = (status: string) => {
    setSelectedStatus((prevStatus) =>
      prevStatus.includes(status)
        ? prevStatus.filter((s) => s !== status)
        : [...prevStatus, status]
    );
  };

  // Method 토글
  const handleMethodToggle = (method: string) => {
    setSelectedMethod((prevMethod) =>
      prevMethod.includes(method)
        ? prevMethod.filter((m) => m !== method)
        : [...prevMethod, method]
    );
  };

  const handleBarClick = (start: string, end: string) => {
    setFromDate(start);
    setToDate(end);
    setSelectedDate("");
  };

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
  const selectClass =
    "cursor-pointer outline-none border border-[#cecece] pl-3 pr-4 py-1.5 rounded appearance-none disabled:text-[#727272] bg-no-repeat mr-3";
  const dateClass =
    "cursor-pointer border border-[#cecece] px-2.5 h-[37.6px] rounded disabled:border-[#dbdbdb] disabled:text-[#858585] mr-2";
  const refreshClass =
    "cursor-pointer bg-[#eeeeee] w-[37.6px] h-[37.6px] rounded ml-1 hover:bg-[#e6e6e6] transition-background duration-200";

  return (
    <>
      <div className={`${flexClass} mb-5`}>
        <div className={flexClass}>
          <select
            onChange={handleProjectChange}
            value={selectedProjectId ?? ""}
            className={`${selectClass} w-56`}
            style={{
              backgroundImage: `url('/selectIcon.png')`,
              backgroundPosition: "right 12px center",
              backgroundSize: "9px 13px",
              paddingRight: "36px",
            }}
          >
            <option value="" disabled>
              프로젝트 선택
            </option>
            {projects?.content.map((project: Project) => (
              <option key={project.id} value={project.id}>
                {project.projectName}
              </option>
            ))}
          </select>

          <select
            onChange={handleDeployChange}
            value={selectedDeployId ?? ""}
            className={`${selectClass} w-56`}
            disabled={!selectedProjectId || isLoading}
            style={{
              backgroundImage: `url('/selectIcon.png')`,
              backgroundPosition: "right 12px center",
              backgroundSize: "9px 13px",
              paddingRight: "36px",
            }}
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
            className={`${selectClass} w-32`}
            style={{
              backgroundImage: `url('/selectIcon.png')`,
              backgroundPosition: "right 12px center",
              backgroundSize: "9px 13px",
              paddingRight: "36px",
            }}
          >
            <option value="">직접 선택</option>
            <option value="today">오늘</option>
            <option value="week">이번 주</option>
            <option value="total">전체</option>
          </select>

          <div className="flex">
            <input
              type="datetime-local"
              value={fromDate}
              onChange={(e) => {
                setFromDate(formatTimestamp(e.target.value));
              }}
              onClick={() => setSelectedDate("")}
              className={`${dateClass} w-60`}
            />
            <input
              type="datetime-local"
              value={toDate}
              onChange={(e) => setToDate(formatTimestamp(e.target.value))}
              onClick={() => setSelectedDate("")}
              className={`${dateClass} w-64`}
            />
          </div>

          <IoRefresh
            color="#575757"
            onClick={handleRefresh}
            className={`${refreshClass} ${flexClass} p-2`}
          />
        </div>
      </div>

      <div className="flex mb-5">
        <Monitoring selectedDeployId={selectedDeployId} />
        <HistogramChart
          histograms={histograms}
          intervalMinute={intervalMinute}
          fromDate={fromDate}
          toDate={toDate}
          onBarClick={handleBarClick}
        />
      </div>

      <div className="w-full border rounded p-4 hover:shadow-lg transition-shadow duration-200">
        <div className="text-lg mb-3 font-semibold">타입별 로그 데이터</div>
        <div className="flex flex-wrap">
          <select
            onChange={handleTypeChange}
            value={selectedType}
            className={`${selectClass} h-[37.6px] w-40 mr-24`}
            style={{
              backgroundImage: `url('/selectIcon.png')`,
              backgroundPosition: "right 12px center",
              backgroundSize: "9px 13px",
              paddingRight: "36px",
            }}
          >
            <option value="method">Method</option>
            <option value="status">Status</option>
          </select>

          <DoughnutChart
            selectedType={selectedType}
            counts={chartData || {}}
            logs={logData?.content || []}
            fetchMoreLogs={fetchMoreLogs}
            hasMoreLogs={hasMoreLogs}
            isRefresh={currentPage === 0}
            selectedStatus={selectedStatus}
            selectedMethod={selectedMethod}
            handleStatusToggle={handleStatusToggle}
            handleMethodToggle={handleMethodToggle}
          />
        </div>
      </div>
    </>
  );
}
