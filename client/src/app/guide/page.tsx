import React from "react";
import Button from "../../components/Button";
import Link from "next/link";
import { routes } from "../../constants/routeURL";
import { FaReact, FaGithub } from "react-icons/fa";
import GuideSection from "./components/GuideSection";

const FeatureCard: React.FC<{
  title: string;
  items: string[];
  icon: React.ReactNode;
}> = ({ title, items, icon }) => (
  <div className="p-6 transition-shadow duration-300">
    <div className="flex items-center mb-4">
      <div className="text-blue-500 mr-3">{icon}</div>
      <h2 className="text-xl font-semibold">{title}</h2>
    </div>
    <ul className="list-disc pl-5 space-y-2">
      {items.map((item, index) => (
        <li key={index}>{item}</li>
      ))}
    </ul>
  </div>
);

const guideSections = [
  {
    id: "ttalkak",
    title: "Ttalkak",
    content: (
      <>
        <p className="leading-relaxed mb-4">
          Ttalkak은 주니어 개발자들을 위한 간편 배포 서비스입니다. Docker
          컨테이너 기반으로 프론트엔드, 백엔드, 데이터베이스를 쉽고 빠르게
          배포할 수 있습니다. GitHub 연동을 통해 자동으로 Dockerfile을 생성하고,
          블록체인 기반 결제를 통해 실제 사용량에 따른 요금이 청구됩니다. 도메인
          자동 생성 기능도 제공되어 배포 과정이 더욱 간편해집니다.
        </p>
        <div className="grid gap-6">
          <FeatureCard
            title="주요 기능"
            icon={<FaReact size={24} />}
            items={[
              "프론트엔드 배포 (React.js, Next.js, CRA, Vite)",
              "백엔드 배포 (Java Spring)",
              "데이터베이스 배포 (MySQL, PostgreSQL, Redis, MongoDB, MariaDB)",
            ]}
          />
          <FeatureCard
            title="특징"
            icon={<FaGithub size={24} />}
            items={[
              "GitHub 소스코드 기반 자동 Dockerfile 생성",
              "블록체인 결제 시스템",
              "도메인 자동 생성 및 관리",
            ]}
          />
        </div>
      </>
    ),
  },
  {
    id: "license",
    title: "라이센스 및 저작권",
    content: (
      <p>
        소스코드 제공 및 사용 규정에 따라 상업적 용도로 사용 시 제한이 있습니다.
      </p>
    ),
  },
  {
    id: "support",
    title: "지원 및 문의",
    content: (
      <p>
        기술 지원이 필요한 경우 sunsuking@gmail.com으로 이메일을 보내주세요.
      </p>
    ),
  },
];

interface GuideCardProps {
  title: string;
  description: string;
  link: string;
  icon?: React.ReactElement;
}

const GuideCard: React.FC<GuideCardProps> = ({ title, description, link }) => {
  return (
    <Link href={link}>
      <div className="bg-white p-8 rounded-lg border min-h-[100px] lg:min-h-[200px] hover:shadow-lg transition-shadow">
        <h3 className="text-xl font-semibold mb-2">{title}</h3>
        <p>{description}</p>
      </div>
    </Link>
  );
};

export default function GuidePage() {
  return (
    <div className="container p-8 max-h-screen pb-8 ">
      <div className="flex-grow mx-auto px-4 sm:px-6 lg:px-8 pt-16">
        <h1 className="text-4xl font-bold mb-4 text-center">딸깍 가이드</h1>
        <section className="text-center border-b pb-12 mb-12">
          <p className=" max-w-2xl mx-auto">
            딸깍은 Frontend, Backend, Database를 배포하고
          </p>
          <p className="mb-8 max-w-2xl mx-auto">
            더 빠르고 개인화된 웹을 구축, 확장 및 보호할 수 있는 개발자 경험과
            인프라를 제공합니다.
          </p>
          <div className="flex flex-col sm:flex-row justify-center items-center space-y-4 sm:space-y-0 sm:space-x-4">
            <Link href="/project">
              <Button label="프로젝트 등록하기" primary size="large" />
            </Link>
          </div>
        </section>
      </div>

      <section className="mb-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-4 mx-12">
          <GuideCard
            title="Frontend 가이드"
            description="React.js, Next.js 기반 서비스를 배포하는 방법을 알아보세요."
            link={routes.guide.frontinfo}
          />
          <GuideCard
            title="Backend 가이드"
            description="Java Spring 기반 서비스를 배포하는 방법을 알아보세요."
            link={routes.guide.backinfo}
          />
          <GuideCard
            title="Database 가이드"
            description="MySQL, PostgreSQL, Redis, MongoDB, MariaDB 를 배포하는 방법을 알아보세요."
            link={routes.guide.dbinfo}
          />
        </div>
      </section>

      <div className="max-w-6xl mx-auto p-5">
        {guideSections.map((section, index) => (
          <GuideSection key={index} title={section.title}>
            {section.content}
          </GuideSection>
        ))}
      </div>
      <div className="h-20"></div>
    </div>
  );
}
