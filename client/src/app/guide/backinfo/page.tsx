"use client";

import React, { useState } from "react";
import { FaGithub, FaJava, FaDocker, FaGlobe, FaServer } from "react-icons/fa";
import GuideSection from "../components/GuideSection";
import { Tab } from "../components/Tab";
import { CodeBlock } from "../components/CodeBlock";
import { SiGradle, SiApachemaven } from "react-icons/si";
import Link from "next/link";
import { IconType } from "react-icons";

interface BuildToolOption {
  label: string;
  Icon: IconType;
  code: string;
}

interface GuideStepProps {
  id: string;
  icon: React.ReactElement;
  title: string;
  description: string;
}

const StepItem: React.FC<GuideStepProps> = ({ icon, title, description }) => (
  <div className="mb-6 min-w-full">
    <div className="flex items-center mb-2">
      <div className="text-blue-500 mr-3">{icon}</div>
      <h3 className="text-lg font-semibold">{title}</h3>
    </div>
    <p className="text-gray-600 ml-9">{description}</p>
  </div>
);

const gradleOptions: BuildToolOption[] = [
  {
    label: "Gradle",
    Icon: SiGradle,
    code: `FROM gradle:8.8-jdk17 AS build
            WORKDIR /app
            COPY . .
            RUN gradle clean build -x test --no-daemon

            FROM eclipse-temurin:17-jdk
            WORKDIR /app
            COPY --from=build /app/build/libs/*.jar /app/app.jar
            ENTRYPOINT ["java", "-jar", "/app/app.jar"]`,
  },
];

const mavenOptions: BuildToolOption[] = [
  {
    label: "Maven",
    Icon: SiApachemaven,
    code: `FROM maven:3.9.4-eclipse-temurin-17 AS build
          WORKDIR /app
          COPY . .
          RUN mvn clean package -DskipTests

          FROM eclipse-temurin:17-jdk
          WORKDIR /app
          COPY --from=build /app/target/*.jar /app/app.jar
          ENTRYPOINT ["java", "-jar", "/app/app.jar"]`,
  },
];

const steps: GuideStepProps[] = [
  {
    id: "github-repo",
    icon: <FaGithub size={24} />,
    title: "GitHub 레포지토리 연동",
    description: "배포할 백엔드 프로젝트의 GitHub 레포지토리를 선택합니다.",
  },
  {
    id: "root-directory",
    icon: <FaServer size={24} />,
    title: "루트 디렉토리 설정",
    description:
      "build.gradle 또는 pom.xml 파일이 있는 루트 디렉토리 경로를 설정합니다.",
  },
  {
    id: "java-version",
    icon: <FaJava size={24} />,
    title: "Java 버전 설정",
    description: "프로젝트에 적합한 Java 버전을 입력합니다.",
  },
  {
    id: "dockerfile",
    icon: <FaDocker size={24} />,
    title: "Dockerfile 확인",
    description:
      "기존 Dockerfile이 있다면 사용되며, 없을 경우 자동으로 생성됩니다. 부적합 시 에러가 표시됩니다.",
  },
  {
    id: "domain-port",
    icon: <FaGlobe size={24} />,
    title: "도메인 및 포트 매핑",
    description:
      "배포가 완료되면 '{프로젝트이름}.ttalkak.com' 형식의 도메인이 자동으로 생성되며, 포트 매핑이 수행됩니다.",
  },
];

const BackendGuidePage: React.FC = () => {
  const container = "px-4 py-10 border-t bg-white";
  const containerTitle = "text-2xl font-bold mb-4";

  const [selectedGradle, setSelectedGradle] = useState<BuildToolOption>(
    gradleOptions[0]
  );
  const [selectedMaven, setSelectedMaven] = useState<BuildToolOption>(
    mavenOptions[0]
  );

  return (
    <div className="container px-8 min-h-screen max-h-screen">
      <div className="flex-grow px-4 sm:px-6 lg:px-8 pt-16 grid">
        <div className="flex flex-col justify-center h-40 mb-6">
          <h2 className="text-4xl font-bold mb-4 text-center">
            백엔드 배포 가이드
          </h2>
          <div className="text-center mt-2 mb-4 text-gray-600">
            Ttalkak 서비스에서 백엔드 배포 과정에 대해 안내합니다.
          </div>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>시작하기 전에</h2>
          <p className="text-gray-700 mb-4">
            Ttalkak을 사용하여 백엔드 프로젝트를 배포하기 전에 다음 사항을
            확인하세요:
          </p>
          <ul className="list-disc space-y-3 pl-6 mt-2">
            <li>GitHub 저장소에 프로젝트가 푸시되어 있어야 합니다.</li>
            <li>
              프로젝트 루트에 build.gradle 또는 pom.xml 파일이 있어야 합니다.
            </li>
            <li>Dockerfile이 없는 경우, Ttalkak이 자동으로 생성합니다.</li>
            <div>
              <p className="text-red-500 font-bold">
                [!] Database가 필요한 프로젝트의 경우 Database를 먼저 배포해
                주세요.
              </p>
              <p>
                자세한 사항은{" "}
                <Link href="/guide/dbinfo" className="text-blue-500 underline">
                  Database 가이드
                </Link>{" "}
                를 참고하세요
              </p>
            </div>
          </ul>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>Gradle 빌드 도구 선택</h2>
          <div className="flex space-x-2 mb-4">
            {gradleOptions.map((option) => (
              <Tab
                key={option.label}
                label={option.label}
                Icon={option.Icon}
                isActive={selectedGradle.label === option.label}
                onClick={() => setSelectedGradle(option)}
              />
            ))}
          </div>
          <CodeBlock code={selectedGradle.code} />
        </div>

        <div className={container}>
          <h2 className={containerTitle}>Maven 빌드 도구 선택</h2>
          <div className="flex space-x-2 mb-4">
            {mavenOptions.map((option) => (
              <Tab
                key={option.label}
                label={option.label}
                Icon={option.Icon}
                isActive={selectedMaven.label === option.label}
                onClick={() => setSelectedMaven(option)}
              />
            ))}
          </div>
          <CodeBlock code={selectedMaven.code} />
        </div>

        <div className={container}>
          <h2 className={containerTitle}>환경 변수 설정</h2>
          <p className="text-gray-700 mb-4">
            배포 전 필요한 환경 변수를 설정하세요. 예를 들어 데이터베이스 URL,
            API 키 등을 설정할 수 있습니다.
          </p>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>배포 로그 및 실시간 확인</h2>
          <p className="text-gray-700 mb-4">
            배포가 진행되면서 실시간으로 로그를 확인할 수 있습니다. 배포 상태를
            모니터링하면서 필요한 조치를 취할 수 있습니다.
          </p>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>도메인 및 포트 매핑</h2>
          <p className="text-gray-700 mb-4">
            배포가 완료되면 &ldquo;프로젝트이름.ttalkak.com &rdquo;형식의
            도메인이 생성되며, 포트가 매핑됩니다.
          </p>
        </div>

        <GuideSection title="최적화 팁">
          <ul className="list-disc pl-6">
            <li>
              빌드 시간을 줄이기 위해 .dockerignore 파일을 사용하여 불필요한
              파일을 제외하세요.
            </li>
            <li>
              프로덕션 빌드 시 적절한 JVM 옵션을 설정하여 성능을 최적화하세요.
            </li>
            <li>환경별 설정을 위해 환경 변수를 적극 활용하세요.</li>
            <li>데이터베이스 연결 풀 및 캐싱 전략을 최적화하세요.</li>
          </ul>
        </GuideSection>

        <GuideSection title="주의사항">
          <ul className="list-disc pl-6">
            <li>프로젝트 이름은 영문으로 설정해야 합니다.</li>
            <li>
              Ttalkak은 단일 컨테이너만 지원합니다 (Docker Compose 미지원).
            </li>
            <li>
              배포된 애플리케이션의 사용량에 따라 자동으로 요금이 청구됩니다.
            </li>
            <li>
              보안 관련 정보(API 키, 데이터베이스 비밀번호 등)는 환경 변수로
              관리하고, 코드에 직접 포함시키지 마세요.
            </li>
          </ul>
        </GuideSection>

        <div className="h-20"></div>
      </div>
    </div>
  );
};

export default BackendGuidePage;
