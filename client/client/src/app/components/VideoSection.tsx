"use client";

import Image from "next/image";
import { useRef, useEffect } from "react";
import { motion } from "framer-motion";
import { ImArrowUp2 } from "react-icons/im";

interface VideoSectionProps {
  videoSrc: string;
  thumbnailSrc: string;
  text: {
    title: string;
    subtitle: string;
    description: string[];
  };
  isActive: boolean;
  onScrollToTop: () => void;
}

export default function VideoSection({
  videoSrc,
  thumbnailSrc,
  text,
  isActive,
  onScrollToTop,
}: VideoSectionProps) {
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
      <Image
        src={thumbnailSrc}
        alt={text.title}
        fill
        priority
        style={{ objectFit: "cover" }}
      />
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
            className="text-5xl md:text-5xl lg:text-6xl font-bold mb-12"
          >
            {text.title}
          </motion.h2>
          <motion.p
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: isActive ? 1 : 0, y: isActive ? 0 : 20 }}
            transition={{ duration: 1, delay: 1 }}
            className="md:text-2xl lg:text-3xl mb-8"
          >
            {text.subtitle}
          </motion.p>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: isActive ? 1 : 0, y: isActive ? 0 : 20 }}
            transition={{ duration: 1, delay: 1.5 }}
            className="text-base lg:text-lg max-w-2xl text-white/85"
          >
            {text.description &&
              text.description.map((line, index) => (
                <p key={index} className="mt-1 ">
                  {line}
                </p>
              ))}
          </motion.div>
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
}
