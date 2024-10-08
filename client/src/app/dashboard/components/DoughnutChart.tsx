import { Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import React, { useEffect, useState, useRef } from "react";
import { formatTimestamp } from "@/utils/getDate";

ChartJS.register(ArcElement, Tooltip, Legend);

// status별 색상 정의
const statusBackgroundColors = [
  "#A4DFDF", // 2XX
  "#9AD0F5", // 3XX
  "#FFE6AA", // 4XX
  "#FFB0C1", // 5XX
];

// method별 색상 정의
const methodBackgroundColors = [
  "#FFB0C1", // DELETE
  "#FFE6AA", // POST
  "#A4DFDF", // GET
  "#9AD0F5", // PATCH
  "#CCB2FF", // PUT
];

const getPercentageData = (
  counts: Record<string, number>,
  selectedType: string
) => {
  const total = Object.values(counts).reduce((sum, value) => sum + value, 0);
  if (total === 0) {
    return {
      data: [1], // 데이터가 없을 때 기본 원형
      labels: ["No Data"],
      backgroundColor: ["#f5f5f5"],
    };
  }

  const backgroundColors =
    selectedType === "status" ? statusBackgroundColors : methodBackgroundColors;

  return {
    data: Object.values(counts),
    labels: Object.keys(counts),
    backgroundColor: backgroundColors,
  };
};

const DoughnutChart = ({
  selectedType,
  counts,
  logs,
  fetchMoreLogs,
  hasMoreLogs,
  isRefresh,
  selectedStatus,
  selectedMethod,
  handleStatusToggle,
  handleMethodToggle,
}: {
  selectedType: string;
  counts: Record<string, number>;
  logs: any[];
  fetchMoreLogs: () => void;
  hasMoreLogs: boolean;
  isRefresh: boolean;
  selectedStatus: string[];
  selectedMethod: string[];
  handleStatusToggle: (status: string) => void; // status 토글 핸들러
  handleMethodToggle: (method: string) => void;
}) => {
  const [sortedLogs, setSortedLogs] = useState<any[]>([]);
  const [isFetching, setIsFetching] = useState(false);
  const logsContainerRef = useRef<HTMLDivElement>(null);

  // 최신순으로 로그 정렬
  useEffect(() => {
    const sorted = logs.sort((a, b) => {
      const dateA = new Date(a.timestamp).getTime();
      const dateB = new Date(b.timestamp).getTime();
      return dateB - dateA;
    });
    setSortedLogs(sorted);

    // 새로고침일 때만 스크롤을 상단으로 이동
    if (isRefresh && logsContainerRef.current) {
      logsContainerRef.current.scrollTop = 0;
    }
  }, [logs, isRefresh]); // isRefresh에 의존하여 새로고침 시만 처리

  // 무한 스크롤
  const handleScroll = () => {
    if (logsContainerRef.current && !isFetching && hasMoreLogs) {
      const { scrollTop, scrollHeight, clientHeight } =
        logsContainerRef.current;

      const percentage = (scrollTop / (scrollHeight - clientHeight)) * 100;

      if (percentage >= 80) {
        setIsFetching(true);
        fetchMoreLogs();

        setTimeout(() => {
          setIsFetching(false);
        }, 2000);
      }
    }
  };

  const chartData = getPercentageData(counts, selectedType);

  const data = {
    labels: chartData.labels,
    datasets: [
      {
        data: chartData.data,
        backgroundColor: chartData.backgroundColor,
        borderColor: chartData.backgroundColor,
      },
    ],
  };

  const options = {
    plugins: {
      tooltip: {
        callbacks: {
          label: function (context: any) {
            const label = context.label || "";
            const value = context.raw;
            const total = context.dataset.data.reduce(
              (sum: number, val: number) => sum + val,
              0
            );
            const percentage = ((value / total) * 100).toFixed(2) + "%";
            return `${label}: ${value} (${percentage})`;
          },
        },
      },
      legend: {
        display: false,
      },
    },
  };

  const btn = "rounded px-4 py-[5px] mr-2.5";

  return (
    <>
      <div className="min-w-[500px] ml-8 h-[37.6px] flex items-center">
        {selectedType === "status" ? (
          <>
            {Object.keys(counts).map((status, index) => (
              <button
                key={status}
                className={btn}
                style={{
                  backgroundColor: selectedStatus.includes(status[0])
                    ? statusBackgroundColors[index]
                    : "#ececec",
                  color: selectedStatus.includes(status[0])
                    ? "white"
                    : "#8b8b8b",
                }}
                onClick={() => handleStatusToggle(status[0])}
              >
                {status[0]}00
              </button>
            ))}
          </>
        ) : (
          <>
            {Object.keys(counts).map((method, index) => (
              <button
                key={method}
                className={btn}
                style={{
                  backgroundColor: selectedMethod.includes(method)
                    ? methodBackgroundColors[index]
                    : "lightgray",
                  color: selectedMethod.includes(method) ? "white" : "gray",
                  textDecoration: selectedMethod.includes(method)
                    ? "none"
                    : "line-through",
                }}
                onClick={() => handleMethodToggle(method)}
              >
                {method}
              </button>
            ))}
          </>
        )}
      </div>
      <div className="flex min-w-full py-8">
        <div className="ml-4" style={{ width: "240px", height: "240px" }}>
          <Doughnut data={data} options={options} />
        </div>

        <div
          className="w-2/3 h-60 overflow-y-auto custom-scrollbar w-full ml-7 mr-0.5"
          ref={logsContainerRef}
          onScroll={handleScroll}
        >
          {sortedLogs.length > 0 ? (
            sortedLogs.map((log, index) => (
              <div key={index} className="flex border-b py-2.5 text-sm">
                <div className="px-8 xl:mr-28">
                  {formatTimestamp(log.timestamp)}
                </div>
                <div className="xl:flex">
                  <div className="w-48">
                    <span className="text-[#bdbdbd] mr-3.5">method</span>
                    {log.method}
                  </div>
                  <div className="w-44">
                    <span className="text-[#bdbdbd] mr-3.5">status</span>
                    {log.status}
                  </div>
                  <div className="w-48">
                    <span className="text-[#bdbdbd] mr-3.5">path</span>
                    {log.path}
                  </div>
                  <div className="w-48">
                    <span className="text-[#bdbdbd] mr-3.5">duration</span>
                    {log.duration}s
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="w-full h-full text-[#b0b0b0] bg-gradient-to-br from-[#f5f5f5] rounded via-[#F4F4F5] to-[#f1f1ff] text-[#3b3b3b] flex items-center justify-center">
              조회된 데이터가 없습니다
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default DoughnutChart;
