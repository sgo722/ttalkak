import React from "react";
import {
  FaDownload,
  FaWallet,
  FaServer,
  FaCoins,
  FaQuestionCircle,
  FaCheckCircle,
  FaInfoCircle,
  FaExclamationTriangle,
} from "react-icons/fa";
import Button from "../../../components/Button";
import GuideSection from "../components/GuideSection";

interface GuideStepProps {
  id: string;
  icon: React.ReactElement;
  title: string;
  description: string;
}

const StepItem: React.FC<GuideStepProps> = ({ icon, title, description }) => (
  <div className="mb-8 flex items-start">
    <div className="text-blue-500 mr-4 mt-1">{icon}</div>
    <div>
      <h3 className="text-xl font-semibold mb-2">{title}</h3>
      <p className="text-gray-600">{description}</p>
    </div>
  </div>
);

const ListItem: React.FC<{
  children: React.ReactNode;
  icon: React.ReactElement;
}> = ({ children, icon }) => (
  <li className="flex items-start mb-4">
    <span className="text-blue-500 mr-3 mt-1">{icon}</span>
    <span>{children}</span>
  </li>
);

const AppGuidePage: React.FC = () => {
  const downloadUrl =
    "https://drive.google.com/file/d/18VPEb_-APx4j2zEmuyfnkVvdoOXjAUUD/view?usp=drive_link";
  const dockerUrl = "https://docs.docker.com/desktop/install/windows-install/";

  const steps: GuideStepProps[] = [
    {
      id: "download",
      icon: <FaDownload size={24} />,
      title: "Win64 버전 다운로드",
      description: "딸깍 앱을 공식 웹사이트에서 다운로드합니다.",
    },
    {
      id: "install",
      icon: <FaDownload size={24} />,
      title: "설치 진행",
      description: "다운로드한 파일을 실행하여 설치를 진행합니다.",
    },
    {
      id: "metamask",
      icon: <FaWallet size={24} />,
      title: "MetaMask 연동",
      description: "앱 실행 후 MetaMask 지갑을 연동합니다.",
    },
    {
      id: "start",
      icon: <FaServer size={24} />,
      title: "서비스 시작",
      description: "앱을 실행하여 서비스를 시작합니다.",
    },
  ];

  return (
    <div className="container px-10 max-h-screen pb-16 py-14">
      <h1 className="text-4xl px-4 font-bold text-center mb-10">
        딸깍 데스크톱 가이드
      </h1>

      <section className="text-center border-b pb-12 mb-16">
        <p className=" max-w-2xl mx-auto">
          딸깍은 Frontend, Backend, Database를 배포하고 더 빠르고 개인화된 웹을
          구축, 확장 및 보호할 수 있는 개발자 경험과 인프라를 제공합니다.
        </p>
        <div className="flex justify-center mt-8">
          <a href={downloadUrl} download>
            <Button label="데스크톱 Download" primary size="large" />
          </a>
        </div>
      </section>

      <GuideSection title="시작하기 전에">
        <div className="space-y-6">
          <p className="text-red-500 font-bold mb-3">
            딸깍 데스크톱 앱을 설치하기 전, 아래 사항을 꼭 확인해주세요.
          </p>
          <p>
            딸깍 데스크톱은 Windows 환경에서 Docker 기반으로 실행됩니다. 앱 실행
            전에 Docker Desktop이 설치되어 있는지 확인해 주세요.
          </p>
          <a href={dockerUrl} className="block mt-4">
            <Button size="small" label="Docker Desktop 설치하기" primary />
          </a>

          <ul className="space-y-4">
            <ListItem icon={<FaCheckCircle />}>
              Windows 64비트 운영 체제를 사용하고 있는지 확인하세요.
            </ListItem>
            <ListItem icon={<FaCheckCircle />}>
              MetaMask 지갑 주소를 확인하세요.
            </ListItem>
            <ListItem icon={<FaCheckCircle />}>
              안정적인 인터넷 연결이 필요합니다.
            </ListItem>
          </ul>
        </div>
      </GuideSection>

      <GuideSection title="설치 과정">
        <div className="space-y-10">
          {steps.map((step) => (
            <StepItem key={step.id} {...step} />
          ))}
        </div>
      </GuideSection>

      <GuideSection title="주요 기능">
        <ul className="space-y-4">
          <ListItem icon={<FaInfoCircle />}>실시간 서비스 할당</ListItem>
          <ListItem icon={<FaInfoCircle />}>사용량 기반 요금 계산</ListItem>
          <ListItem icon={<FaInfoCircle />}>MetaMask를 통한 코인 정산</ListItem>
        </ul>
      </GuideSection>

      <GuideSection title="MetaMask 연동">
        <p className="mb-6">
          Ttalkak 앱에서 회원가입시, MetaMask 지갑주소를 입력해야 합니다. 이를
          통해 서비스 이용 요금을 투명하게 정산받을 수 있습니다.
        </p>
        <p className="mb-4">
          잘못된 주소를 입력한 경우 sunsuking@gmail.com으로 문의해주세요.
        </p>
      </GuideSection>

      <GuideSection title="요금 정책">
        <ul className="space-y-4">
          <ListItem icon={<FaCoins />}>
            서비스 사용량에 따라 5분 주기로 실시간으로 요금이 계산됩니다.
          </ListItem>
          <ListItem icon={<FaCoins />}>
            계산된 요금은 코인으로 환산되어 MetaMask 지갑으로 정산됩니다.
          </ListItem>
        </ul>
      </GuideSection>

      <GuideSection title="지원 및 문의">
        <ul className="space-y-4">
          <ListItem icon={<FaQuestionCircle />}>
            설치 또는 사용 중 문제가 발생하면 sunsuking@gmail.com으로
            문의해주세요.
          </ListItem>
          <ListItem icon={<FaQuestionCircle />}>
            최신 업데이트 및 공지사항은 공식 웹사이트에서 확인하세요.
          </ListItem>
        </ul>
      </GuideSection>

      <GuideSection title="주의사항">
        <ul className="space-y-4">
          <ListItem icon={<FaExclamationTriangle />}>
            개인 MetaMask 키는 절대로 공유하지 마세요.
          </ListItem>
          <ListItem icon={<FaExclamationTriangle />}>
            안전한 네트워크 환경에서 앱을 사용해주세요.
          </ListItem>
        </ul>
      </GuideSection>
      <div className="h-20"></div>
    </div>
  );
};

export default AppGuidePage;
