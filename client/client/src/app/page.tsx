"use client";

import { useState, useEffect, useRef } from "react";
import { throttle } from "lodash";
import { motion, AnimatePresence } from "framer-motion";
import { ImArrowDown2, ImArrowUp2 } from "react-icons/im";

const videoSources = [
  "/videos/earth.mp4",
  "/videos/log.mp4",
  "/videos/pay.mp4",
  "/videos/block.mp4",
];

const texts = [
  {
    title: "간편한 배포 서비스",
    subtitle: "클릭만으로 완성되는 배포 자동화",
    description:
      "이제 복잡한 배포 과정에서 벗어나세요. 우리 서비스는 클릭 몇 번으로 자동으로 모든 배포 단계를 처리하여, 신속하고 안정적인 배포 환경을 제공합니다. 간단한 설정만으로 복잡한 인프라 관리 없이도 배포를 완료할 수 있으며, 효율적인 배포 프로세스를 통해 시간과 리소스를 절약할 수 있습니다.",
  },
  {
    title: "AI 기반 실시간 로그 분석",
    subtitle: "비즈니스 성공을 위한 필수 도구",
    description:
      "AI 기반 실시간 로그 분석은 이제 단순한 데이터 관리 도구를 넘어 비즈니스 전략을 지원하는 핵심 기술로 자리 잡고 있습니다. 이 기술을 통해 기업은 더 나은 의사결정을 내리고, 운영 최적화와 비용 절감을 실현할 수 있습니다. AI가 제공하는 예리한 인사이트는 기업이 경쟁에서 한발 앞서나가는 데 필수적인 차별화된 경쟁력이 될 것입니다.",
  },
  {
    title: "최적의 사용, 정확한 결제",
    subtitle: "스마트한 결제 시스템의 결합",
    description:
      "사용자는 자신이 소비한 만큼만 요금을 지불하면서도, 자동 결제 설정을 통해 결제 과정에서 발생할 수 있는 번거로움을 없앨 수 있습니다. 이러한 스마트 결제 시스템은 서비스 운영에 있어 편리함과 경제성을 동시에 제공합니다.",
  },
  {
    title: "블록체인 기반 수익 창출",
    subtitle: "자원 공유로 얻는 디지털 보상",
    description:
      "유휴 자원을 공유해 소득을 창출하세요. 당신의 컴퓨터가 서버가 되어 블록체인 기반으로 디지털 코인을 얻을 수 있는 혁신적인 기회를 제공합니다",
  },
];

const NavigationMenu: React.FC<{
  currentSlide: number;
  setCurrentSlide: (slide: number) => void;
}> = ({ currentSlide, setCurrentSlide }) => {
  return (
    <div className="fixed right-10 top-1/2 transform -translate-y-1/2 z-50">
      <ul className="space-y-4">
        {texts.map((text, index) => (
          <li key={index} className="flex items-center justify-end">
            <button
              onClick={() => setCurrentSlide(index + 1)}
              className={`text-right pr-3 py-2 transition-all duration-300 flex items-center ${
                currentSlide === index + 1
                  ? "text-white font-bold"
                  : "text-gray-400 hover:text-gray-200"
              }`}
            >
              <span className="mr-3">{text.title}</span>
              <span
                className={`w-2 rounded-full ${
                  currentSlide === index + 1
                    ? "bg-white border-white h-5"
                    : "bg-gray-400 h-2"
                }`}
              ></span>
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

const VideoSection: React.FC<{
  videoSrc: string;
  text: (typeof texts)[0];
  isActive: boolean;
  onScrollToTop: () => void;
}> = ({ videoSrc, text, isActive, onScrollToTop }) => {
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    if (isActive && videoRef.current) {
      videoRef.current.currentTime = 0;
      videoRef.current.play();
    } else if (videoRef.current) {
      videoRef.current.pause();
    }
  }, [isActive]);

  return (
    <div className="relative w-full h-full flex items-center justify-center">
      <video
        ref={videoRef}
        src={videoSrc}
        loop
        muted
        playsInline
        className="absolute top-0 left-0 w-full h-full object-cover"
      />
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: isActive ? 1 : 0 }}
        transition={{ duration: 1 }}
        className="absolute inset-0 bg-black bg-opacity-50 flex items-center justify-start px-24"
      >
        <div className="text-white max-w-4xl px-4">
          <motion.h2
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: isActive ? 1 : 0, y: isActive ? 0 : 20 }}
            transition={{ duration: 1, delay: 0.5 }}
            className="text-4xl md:text-6xl font-bold mb-12"
          >
            {text.title}
          </motion.h2>
          <motion.p
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: isActive ? 1 : 0, y: isActive ? 0 : 20 }}
            transition={{ duration: 1, delay: 1 }}
            className="text-xl md:text-2xl mb-8"
          >
            {text.subtitle}
          </motion.p>
          <motion.p
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: isActive ? 1 : 0, y: isActive ? 0 : 20 }}
            transition={{ duration: 1, delay: 1.5 }}
            className="text-lg max-w-2xl"
          >
            {text.description}
          </motion.p>
        </div>
      </motion.div>
      <motion.button
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        exit={{ opacity: 0, scale: 0.9 }}
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.95 }}
        onClick={onScrollToTop}
        className="fixed right-12 bottom-20 bg-white bg-opacity-20 p-4 rounded-full text-white hover:bg-opacity-30 transition-all duration-300 z-50"
      >
        <ImArrowUp2 size={24} />
      </motion.button>
    </div>
  );
};

const IntroSection: React.FC<{
  isActive: boolean;
  onNextSlide: () => void;
}> = ({ isActive, onNextSlide }) => {
  return (
    <div className="w-full h-full flex flex-col items-center justify-between py-32 bg-gradient-to-r from-blue-500 to-purple-600">
      <div className="flex-grow flex items-center justify-center">
        <div className="text-white text-center max-w-4xl px-4">
          <motion.h1
            initial={{ y: -50, opacity: 0 }}
            animate={{ y: isActive ? 0 : -50, opacity: isActive ? 1 : 0 }}
            transition={{ duration: 1, delay: 0.5 }}
            className="text-5xl md:text-7xl font-bold mb-14"
          >
            간편한 배포 서비스
          </motion.h1>
          <motion.p
            initial={{ y: 50, opacity: 0 }}
            animate={{ y: isActive ? 0 : 50, opacity: isActive ? 1 : 0 }}
            transition={{ duration: 1, delay: 1 }}
            className="text-xl md:text-2xl mb-12"
          >
            가난한.. 개발자들 모여라.. 우리에겐 Ttalkak이 있는데 AWS? Lamda?
            그게 왜 필요하죠? 우리에겐 Ttalkak이 있는데 도커 파일을 내가 어떻게
            써..? 소스코드 기반 도커 파일 생성과 배포 깃허브 기반으로 Project를
            배포 프론트 엔드만 배포하기 아쉽지 않나요 백엔드와 DB 까지? 주니어
            개발자들을 위한 서비스 도메인 구매하기 비싸지 않나요? 도메인
            자동생성 프로젝트 배포 원스톱 서비스 Ttalkak 버려진 프로젝트 클릭
            몇번으로 살려보세요 언제까지 프로젝트하고 버릴래?
          </motion.p>
          <motion.button
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: isActive ? 1 : 0, scale: isActive ? 1 : 0.9 }}
            transition={{ duration: 1, delay: 1.5 }}
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => console.log("dddd")}
            className="bg-white text-blue-600 font-bold mb-20 py-3 px-8 rounded-full text-lg hover:bg-blue-100 transition duration-300 cursor-pointer"
          >
            여기는 내일 수정 ㄱ
          </motion.button>
        </div>
      </div>
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: isActive ? 1 : 0, y: isActive ? 0 : 20 }}
        transition={{ duration: 1, delay: 2 }}
        className="animate-bounce"
      >
        <motion.button
          onClick={onNextSlide}
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.95 }}
          className="bg-white cursor-pointer bg-opacity-20 p-4 rounded-full text-white hover:bg-opacity-30 transition-all duration-300"
        >
          <ImArrowDown2 size={24} />
        </motion.button>
      </motion.div>
    </div>
  );
};

export default function Home() {
  const [currentSlide, setCurrentSlide] = useState(0);
  const containerRef = useRef<HTMLDivElement>(null);
  const isScrolling = useRef(false);

  const totalSlides = videoSources.length + 1;

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
