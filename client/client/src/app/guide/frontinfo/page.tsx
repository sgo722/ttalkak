"use client";

import React, { useState } from "react";
import { FaGithub, FaNodeJs, FaDocker, FaGlobe } from "react-icons/fa";
import GuideSection from "../components/GuideSection";
import { Tab } from "../components/Tab";
import { CodeBlock } from "../components/CodeBlock";
import { SiNextdotjs, SiVite, SiCreatereactapp } from "react-icons/si";
import Link from "next/link";
import { IconType } from "react-icons";

interface FrameworkOption {
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

const npmOptions: FrameworkOption[] = [
  {
    label: "Vite",
    Icon: SiVite,
    code: `FROM node:20.11.1 AS build
WORKDIR /app
COPY package*.json ./ 
RUN npm ci
COPY . . 
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html 
CMD ["nginx", "-g", "daemon off;"]`,
  },
  {
    label: "Create React App",
    Icon: SiCreatereactapp,
    code: `FROM node:18 AS build
WORKDIR /app
COPY package*.json ./ 
RUN npm ci
COPY . . 
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/build /usr/share/nginx/html 
CMD ["nginx", "-g", "daemon off;"]`,
  },
];

const yarnOptions: FrameworkOption[] = [
  {
    label: "Vite",
    Icon: SiVite,
    code: `FROM node:18 AS build
WORKDIR /app
COPY package.json yarn.lock ./ 
RUN yarn install --frozen-lockfile 
COPY . . 
RUN yarn build
FROM nginx:stable-alpine 
COPY --from=build /app/dist /usr/share/nginx/html 
CMD ["nginx", "-g", "daemon off;"]`,
  },
  {
    label: "Create React App",
    Icon: SiCreatereactapp,
    code: `FROM node:18 AS build
WORKDIR /app
COPY package.json yarn.lock ./ 
RUN yarn install --frozen-lockfile 
COPY . . 
RUN yarn build
FROM nginx:stable-alpine 
COPY --from=build /app/build /usr/share/nginx/html 
CMD ["nginx", "-g", "daemon off;"]`,
  },
];

const nextOptions: FrameworkOption[] = [
  {
    label: "Next.js",
    Icon: SiNextdotjs,
    code: `FROM node:18 AS build
WORKDIR /app
COPY package*.json ./ 
RUN npm install --frozen-lockfile 
COPY . . 
RUN npm run build

FROM node:18-alpine AS runner
WORKDIR /app 
COPY --from=build /app/package*.json ./ 
COPY --from=build /app/.next ./.next 
COPY --from=build /app/public ./public 
RUN npm install --production 
CMD ["npm", "start"]`,
  },
];

const FrontGuidePage: React.FC = () => {
  const container = "px-4 py-10 border-t bg-white";
  const containerTitle = "text-2xl font-bold mb-4";

  const [selectedNpm, setSelectedNpm] = useState<FrameworkOption>(
    npmOptions[0]
  );
  const [selectedYarn, setSelectedYarn] = useState<FrameworkOption>(
    yarnOptions[0]
  );
  const [selectedNext, setSelectedNext] = useState<FrameworkOption>(
    nextOptions[0]
  );

  return (
    <div className="container px-8 min-h-screen max-h-screen">
      <div className="flex-grow px-4 sm:px-6 lg:px-8 pt-16 grid">
        <div className="flex flex-col justify-center h-40 mb-6">
          <h2 className="text-4xl font-bold mb-4 text-center">
            프론트엔드 배포 가이드
          </h2>
          <div className="text-center mt-2 mb-4 text-gray-600">
            Ttalkak 서비스에서 프론트엔드 배포 과정에 대해 안내합니다.
          </div>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>시작하기 전에</h2>
          <p className="text-gray-700 mb-4">
            Ttalkak을 사용하여 프론트엔드 프로젝트를 배포하기 전에 다음 사항을
            확인하세요:
          </p>
          <ul className="list-disc space-y-3 pl-6 mt-2">
            <li>GitHub 저장소에 프로젝트가 푸시되어 있어야 합니다.</li>
            <li>프로젝트 루트에 package.json 파일이 있어야 합니다.</li>
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
                를 참고하세요.
              </p>
            </div>
          </ul>
        </div>

        <div className={container}>
          <h2 className={containerTitle}>npm 기반 빌드 도구 선택</h2>
          <div className="flex space-x-2 mb-6">
            {npmOptions.map((option) => (
              <Tab
                key={option.label}
                label={option.label}
                Icon={option.Icon}
                isActive={selectedNpm.label === option.label}
                onClick={() => setSelectedNpm(option)}
              />
            ))}
          </div>
          <p className="mb-4">
            선택한 빌드 툴에 따라 자동 생성되는 Dockerfile 양식입니다.
          </p>
          <CodeBlock code={selectedNpm.code} />
        </div>

        <div className={container}>
          <h2 className={containerTitle}>yarn 기반 빌드 도구 선택</h2>
          <div className="flex space-x-2 mb-6">
            {yarnOptions.map((option) => (
              <Tab
                key={option.label}
                label={option.label}
                Icon={option.Icon}
                isActive={selectedYarn.label === option.label}
                onClick={() => setSelectedYarn(option)}
              />
            ))}
          </div>
          <p className="mb-4">
            선택한 빌드 툴에 따라 자동 생성되는 Dockerfile 양식입니다.
          </p>
          <CodeBlock code={selectedYarn.code} />
        </div>

        <div className={container}>
          <h2 className={containerTitle}>Next.js 배포</h2>
          <div className="flex space-x-2 mb-6">
            {nextOptions.map((option) => (
              <Tab
                key={option.label}
                label={option.label}
                Icon={option.Icon}
                isActive={selectedNext.label === option.label}
                onClick={() => setSelectedNext(option)}
              />
            ))}
          </div>
          <p className="mb-4">
            선택한 빌드 툴에 따라 자동 생성되는 Dockerfile 양식입니다.
          </p>
          <CodeBlock code={selectedNext.code} />
        </div>

        <GuideSection title="최적화 팁">
          <ul className="list-disc pl-6">
            <li>
              빌드 시간을 줄이기 위해 .dockerignore 파일을 사용하여 불필요한
              파일을 제외하세요.
            </li>
            <li>프로덕션 빌드 시 코드 최소화 및 트리 쉐이킹을 활성화하세요.</li>
            <li>정적 에셋에 대해 CDN을 활용하여 로딩 속도를 개선하세요.</li>
            <li>환경별 설정을 위해 환경 변수를 적극 활용하세요.</li>
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
              보안 관련 정보(API 키 등)는 환경 변수로 관리하고, 코드에 직접
              포함시키지 마세요.
            </li>
          </ul>
        </GuideSection>

        <div className="h-20"></div>
      </div>
    </div>
  );
};

export default FrontGuidePage;
