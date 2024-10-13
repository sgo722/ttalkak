import { useEffect, useRef, useState } from "react";
import MonitoringModal from "./MonitoringModal";
import useGetMonitoring from "@/apis/deploy/useGetMonitoring";
import useTypingAnimation from "@/hooks/useTypingAnimation";

const Monitoring = ({
  selectedDeployId,
}: {
  selectedDeployId: number | null;
}) => {
  const [monitoring, setMonitoring] = useState<string | null>(null);
  const [isAutoScrollEnabled, setIsAutoScrollEnabled] = useState<boolean>(true);
  const containerRef = useRef<HTMLDivElement>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const { data: monitoringData } = useGetMonitoring(
    selectedDeployId || 0,
    !!selectedDeployId
  );

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

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
    <div className="w-[680px] h-52 border mr-4 rounded p-4 hover:shadow-lg transition-shadow duration-200">
      <div className="mb-3 flex justify-between items-center">
        <div className="text-lg font-semibold">AI 분석 결과</div>
        {selectedDeployId && (
          <div
            onClick={openModal}
            className="px-2.5 py-0.5 rounded cursor pointer bg-[#f6f6f6] text-[#747474] hover:bg-[#f3f3f3] hover:text-black transition-background duration-200 cursor-pointer"
          >
            +
          </div>
        )}
      </div>
      <div
        ref={containerRef}
        className="relative p-2 h-[134px] rounded overflow-y-auto custom-scrollbar text-sm bg-gradient-to-br from-[#f5f5f5] via-[#F4F4F5] to-[#f1f1ff] text-[#3b3b3b]"
        onScroll={handleScroll}
      >
        {monitoring ? (
          formattedText
        ) : (
          <div className="relative h-full">
            <div className="absolute w-full h-full flex items-center justify-center text-[#acacac]">
              조회된 데이터가 없습니다
            </div>
            <div className="absolute text-[#cccccc] px-2 py-1">
              <div>모니터링 데이터 분석 결과를 요약합니다.</div>
              <div className="text-[#e3e3e3]">다. 평균 응답 시간은...</div>
            </div>
          </div>
        )}
      </div>
      <MonitoringModal
        formattedText={formattedText}
        isOpen={isModalOpen}
        onClose={closeModal}
      />
    </div>
  );
};

export default Monitoring;
