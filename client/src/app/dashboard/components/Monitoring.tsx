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
    <div className="w-[680px] h-52 border mr-4 rounded p-4 shadow-lg">
      <div className="text-lg mb-3 font-semibold flex">AI 분석 결과</div>
      <div
        ref={containerRef}
        className="relative p-2 h-[134px] rounded overflow-y-auto custom-scrollbar text-sm bg-gradient-to-br from-[#f5f5f5] via-[#F4F4F5] to-[#f1f1ff] text-[#3b3b3b]"
        onScroll={handleScroll}
      >
        {monitoring ? (
          formattedText
        ) : (
          <div className="text-center text-[#b0b0b0] h-full flex items-center justify-center">
            조회된 데이터가 없습니다
          </div>
        )}
      </div>
    </div>
  );
};

export default Monitoring;
