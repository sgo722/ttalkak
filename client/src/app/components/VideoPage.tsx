"use client";

import { useState, useEffect, useRef } from "react";
import { throttle } from "lodash";
import { motion, AnimatePresence } from "framer-motion";
import NavigationMenu from "@/app/components/NavigationMenu";
import VideoSection from "@/app/components/VideoSection";
import IntroSection from "@/app/components/IntroSection";

const videoSources = [
  "/videos/earth.mp4",
  "/videos/log.mp4",
  "/videos/pay.mp4",
  "/videos/block.mp4",
];

const thumbnailSources = [
  "/thumbnails/earth_thumbnail.webp",
  "/thumbnails/log_thumbnail.webp",
  "/thumbnails/pay_thumbnail.webp",
  "/thumbnails/block_thumbnail.webp",
];

export const texts = [
  {
    title: "간편한 배포 서비스",
    subtitle: "클릭만으로 완성되는 배포 자동화",
    description: [
      "간단한 설정으로 복잡한 인프라 관리 없이도 배포를 손쉽게 완료하고,",
      "효율적인 프로세스로 시간과 리소스를 절약할 수 있습니다.",
    ],
  },
  {
    title: "AI 기반 실시간 로그 분석",
    subtitle: "비즈니스 성공을 위한 필수 도구",
    description: [
      "더 나은 의사결정을 내리고, 운영 최적화 및 비용 절감을 실현할 수 있습니다.",
      "AI가 제공하는 예리한 인사이트는 곧 기업의 핵심 경쟁력이 될 것입니다.",
    ],
  },
  {
    title: "최적의 사용, 정확한 결제",
    subtitle: "편의성과 경제성을 고려한 스마트한 결제 시스템",
    description: [
      "사용자는 자신이 소비한 만큼만 요금을 지불하면서도,",
      "자동 결제 설정을 통해 결제 과정에서 발생할 수 있는 번거로움을 없앨 수 있습니다.",
    ],
  },
  {
    title: "블록체인 기반 수익 창출",
    subtitle: "자원 공유로 얻는 디지털 보상",
    description: [
      "유휴 자원을 공유하여 소득을 창출하세요.",
      "당신의 컴퓨터를 서버로 활용해 블록체인 기반의 디지털 코인을 얻을 수 있습니다.",
    ],
  },
];

export default function VideoPage() {
  const [currentSlide, setCurrentSlide] = useState(0);
  const containerRef = useRef<HTMLDivElement>(null);
  const isScrolling = useRef(false);

  const totalSlides = videoSources.length + 1;

  useEffect(() => {
    const footer = document.querySelector("footer");
    if (footer) {
      footer.style.display = "none";
    }
    return () => {
      if (footer) {
        footer.style.display = "block";
      }
    };
  }, []);

  useEffect(() => {
    thumbnailSources.forEach((src) => {
      const img = new Image();
      img.src = src;
    });
  }, []);

  useEffect(() => {
    const handleWheel = throttle((e: WheelEvent) => {
      if (!isScrolling.current) {
        isScrolling.current = true;
        if (e.deltaY > 0 && currentSlide < totalSlides - 1) {
          setCurrentSlide((prev) => prev + 1);
        } else if (e.deltaY < 0 && currentSlide > 0) {
          setCurrentSlide((prev) => prev - 1);
        }
        setTimeout(() => {
          isScrolling.current = false;
        }, 1000);
      }
    }, 100);

    window.addEventListener("wheel", handleWheel);
    return () => window.removeEventListener("wheel", handleWheel);
  }, [currentSlide, totalSlides]);

  return (
    <div className="fixed top-0 left-0 w-screen h-screen overflow-hidden">
      <div ref={containerRef} className="h-full">
        <AnimatePresence>
          {currentSlide === 0 && (
            <motion.div
              key="intro"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              transition={{ duration: 1 }}
              className="absolute top-0 left-0 w-full h-full"
            >
              <IntroSection
                isActive={true}
                onNextSlide={() => setCurrentSlide(1)}
              />
            </motion.div>
          )}
          {videoSources.map(
            (src, index) =>
              currentSlide === index + 1 && (
                <motion.div
                  key={`video-${index}`}
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  exit={{ opacity: 0 }}
                  transition={{ duration: 1 }}
                  className="absolute top-0 left-0 w-full h-full"
                >
                  <VideoSection
                    videoSrc={src}
                    text={texts[index]}
                    thumbnailSrc={thumbnailSources[index]}
                    isActive={true}
                    onScrollToTop={() => setCurrentSlide(0)}
                  />
                </motion.div>
              )
          )}
        </AnimatePresence>
      </div>
      {currentSlide > 0 && (
        <NavigationMenu
          currentSlide={currentSlide}
          setCurrentSlide={setCurrentSlide}
        />
      )}
    </div>
  );
}
