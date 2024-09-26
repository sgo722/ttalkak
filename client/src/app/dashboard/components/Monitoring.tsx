"use client";

import { useEffect, useRef, useState } from "react";
import useGetMonitoring from "@/apis/deploy/useGetMonitoring";
import useTypingAnimation from "../_hooks/useTypingAnimation";

const Monitoring = ({
  selectedDeployId,
}: {
  selectedDeployId: number | null;
}) => {
  const [monitoring, setMonitoring] = useState<string | null>(null);
  const [isAutoScrollEnabled, setIsAutoScrollEnabled] = useState<boolean>(true);
  const containerRef = useRef<HTMLDivElement>(null);

  const { data: monitoringData } = useGetMonitoring(
    selectedDeployId || 0,
    !!selectedDeployId
  );

  useEffect(() => {
    if (monitoringData) setMonitoring(monitoringData.answer);
  }, [monitoringData]);

  const typewriterText = useTypingAnimation(monitoring || "");

  const formattedText = typewriterText.split("\n\n").map((line, index) => (
    <span key={index}>
      {line}
      <br />
      <br />
    </span>
  ));

  // 글씨가 출력될 때 스크롤 최하단으로 이동
  useEffect(() => {
    if (containerRef.current && isAutoScrollEnabled) {
      containerRef.current.scrollTop = containerRef.current.scrollHeight;
    }
  }, [typewriterText, isAutoScrollEnabled]);

  // 글씨가 출력될 때 사용자가 스크롤을 위로 올리면 자동 스크롤을 비활성화
  const handleScroll = () => {
    if (containerRef.current) {
      const isScrolledToBottom =
        containerRef.current.scrollHeight -
          containerRef.current.scrollTop -
          containerRef.current.clientHeight <
        10;
      setIsAutoScrollEnabled(isScrolledToBottom);
    }
  };

  return (
    <div
      ref={containerRef}
      className="w-5/12 h-48 border overflow-y-auto"
      onScroll={handleScroll}
    >
      {monitoring ? formattedText : "No monitoring data available"}
    </div>
  );
};

export default Monitoring;
