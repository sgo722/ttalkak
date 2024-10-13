"use client";

import React from "react";
import GuideSection from "../components/GuideSection";
import Image from "next/image";
import DatabaseConfigGuide from "./components/DatabaseConfigGuide";
import DatabaseDockerGuide from "./components/DatabaseDockerGuide";
import { FaDocker } from "react-icons/fa";

const DatabaseGuidePage: React.FC = () => {
  const container = "px-4 py-10 border-t bg-white";
  const containerTitle = "text-2xl font-bold mb-4";

  return (
    <div className="container px-8 min-h-screen max-h-screen">
      <div className="flex-grow px-4 sm:px-6 lg:px-8 pt-16 grid">
        <div className="flex flex-col justify-center h-40 mb-6">
          <h2 className="text-4xl font-bold mb-4 text-center">
            데이터베이스 배포 가이드
          </h2>
          <div className="text-center mt-2 mb-4 text-gray-600">
            Ttalkak 서비스에서 데이터베이스 배포 과정에 대해 안내합니다.
          </div>
        </div>
        <div className={container}>
          <h2 className={containerTitle}>시작하기 전에</h2>
          <p className="text-gray-700 mb-4">
            Ttalkak을 사용하여 데이터베이스를 배포하기 전에 다음 사항을
            확인하세요:
          </p>
          <ul className="list-disc pl-6 space-y-3 mt-2">
            <li>
              MySQL, Redis, PostgreSQL, MongoDB, MariaDB 데이터베이스를
              제공합니다.
            </li>
            <li>
              <strong>
                보안을 위해 반드시 제공되는 설정을 애플리케이션에 적용해야
                합니다.
              </strong>
            </li>
            <li>각 데이터베이스의 특성을 이해하고 적합한 것을 선택하세요.</li>
          </ul>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>데이터베이스 배포 과정</h2>
          <ul className="list-disc pl-6 space-y-6">
            <li>
              <strong>데이터베이스 선택:</strong> 원하는 데이터베이스를
              선택하고, 데이터베이스 이름을 설정합니다.
              <Image
                src="/image1.png"
                alt="Database Selection"
                width={300}
                height={300}
                className="mt-2"
              />
            </li>
            <li>
              <strong>배포:</strong> &ldquo;생성 &rdquo; 버튼을 클릭하여
              데이터베이스 생성을 시작하고, 실시간으로 배포 과정을 확인할 수
              있습니다.
            </li>
            <li>
              <strong>접속 정보 확인 및 적용:</strong> 배포 완료 후 접속 URL,
              포트, 사용자 이름, 비밀번호 정보를 안전하게 저장하고,
              애플리케이션에 설정하세요.
              <Image
                src="/image3.png"
                alt="Deployment"
                width={300}
                height={300}
                className="mt-2"
              />
            </li>
          </ul>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>데이터베이스 백엔드 설정 가이드</h2>
          <DatabaseConfigGuide />
        </div>

        <div className={container}>
          <h2 className={containerTitle}>
            데이터베이스별 Docker 설정 가이드{" "}
            <FaDocker className="inline ml-2" />
          </h2>
          <DatabaseDockerGuide />
        </div>

        <GuideSection title="보안 권장사항">
          <ul className="list-disc pl-6 space-y-2">
            <li>자동 생성된 비밀번호는 안전한 곳에 보관하세요.</li>
            <li>정기적으로 비밀번호를 변경하는 것이 좋습니다.</li>
            <li>필요한 IP에서만 접근 가능하도록 방화벽 설정을 조정하세요.</li>
            <li>
              <strong>
                데이터베이스 접속 정보를 코드에 하드코딩하지 마세요. 환경 변수를
                사용하세요.
              </strong>
            </li>
          </ul>
        </GuideSection>

        <GuideSection title="요금 정책">
          <ul className="list-disc pl-6 space-y-2">
            <li>사용한 리소스와 시간에 따라 요금이 부과됩니다.</li>
            <li>대시보드에서 현재 사용량과 예상 요금을 확인할 수 있습니다.</li>
            <li>월별 청구서는 이메일로 발송됩니다.</li>
          </ul>
        </GuideSection>

        <GuideSection title="주의사항">
          <ul className="list-disc pl-6 space-y-2">
            <li>데이터베이스 이름은 영문으로 설정해야 합니다.</li>
            <li>서비스는 단일 데이터베이스 인스턴스만 지원합니다.</li>
            <li>
              배포된 데이터베이스 사용량에 따라 자동으로 요금이 청구됩니다.
            </li>
            <li>
              <strong>
                Ttalkak에서 제공하는 데이터베이스 설정을 반드시 사용하세요.
              </strong>
            </li>
          </ul>
        </GuideSection>

        <div className="h-20"></div>
      </div>
    </div>
  );
};

export default DatabaseGuidePage;
