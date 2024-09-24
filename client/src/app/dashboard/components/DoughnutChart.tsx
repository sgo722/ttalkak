"use client";

import { Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import React, { useEffect, useState } from "react";
import { formatTimestamp } from "@/utils/getDate";

ChartJS.register(ArcElement, Tooltip, Legend);

const getPercentageData = (counts: Record<string, number>) => {
  const total = Object.values(counts).reduce((sum, value) => sum + value, 0);
  if (total === 0) {
    return {
      data: [1], // 데이터가 없을 때 기본 원형
      labels: ["No Data"],
      backgroundColor: ["rgba(211, 211, 211, 0.5)"],
      borderColor: ["rgba(211, 211, 211, 1)"],
    };
  }
  return {
    data: Object.values(counts),
    labels: Object.keys(counts),
    backgroundColor: [
      "rgba(255, 99, 132, 0.5)",
      "rgba(54, 162, 235, 0.5)",
      "rgba(255, 206, 86, 0.5)",
      "rgba(75, 192, 192, 0.5)",
      "rgba(153, 102, 255, 0.5)",
    ],
    borderColor: [
      "rgba(255, 99, 132, 1)",
      "rgba(54, 162, 235, 1)",
      "rgba(255, 206, 86, 1)",
      "rgba(75, 192, 192, 1)",
      "rgba(153, 102, 255, 1)",
    ],
  };
};

const DoughnutChart = ({
  title,
  counts,
  logs,
}: {
  title: string;
  counts: Record<string, number>;
  logs: any[];
}) => {
  const [sortedLogs, setSortedLogs] = useState<any[]>([]);

  // 최신순으로 로그 정렬
  useEffect(() => {
    const sorted = logs.sort((a, b) => {
      const dateA = new Date(a.timestamp).getTime();
      const dateB = new Date(b.timestamp).getTime();
      return dateB - dateA;
    });
    setSortedLogs(sorted);
  }, [logs]);

  const chartData = getPercentageData(counts);

  const data = {
    labels: chartData.labels,
    datasets: [
      {
        data: chartData.data,
        backgroundColor: chartData.backgroundColor,
        borderColor: chartData.borderColor,
        borderWidth: 1,
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
    },
  };

  return (
    <div className="flex">
      <div style={{ width: "300px", height: "300px" }}>
        <h3>{title}</h3>
        <Doughnut data={data} options={options} />
      </div>

      <div className="w-2/3 h-80 overflow-y-auto ">
        {sortedLogs.length > 0 ? (
          sortedLogs.map((log, index) => (
            <div key={index} className="flex border-b py-3.5">
              <div className="w-52">{formatTimestamp(log.timestamp)}</div>
              <div>method: {log.method}</div>
              <div>status: {log.status}</div>
              <div>path: {log.path}</div>
              <div>duration: {log.duration}s</div>
              <hr />
            </div>
          ))
        ) : (
          <p>No logs available.</p>
        )}
      </div>
    </div>
  );
};

export default DoughnutChart;
