"use client";
import React, { useState } from "react";
import { FaRegCopy, FaCheck, FaExclamationTriangle } from "react-icons/fa";
import { CodeBlock } from "../../components/CodeBlock";

const CopyButton: React.FC<{ onClick: () => void; copied: boolean }> = ({
  onClick,
  copied,
}) => (
  <button
    onClick={onClick}
    className="absolute top-2 right-2 p-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
  >
    {copied ? <FaCheck /> : <FaRegCopy />}
  </button>
);

const DatabaseConfigGuide: React.FC = () => {
  const [copiedApp, setCopiedApp] = useState<boolean>(false);
  const [copiedEnv, setCopiedEnv] = useState<boolean>(false);

  const appPropertiesConfig = `spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://\${db.host}:\${db.port}/\${db.name}?allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=\${db.username}
spring.datasource.password=\${db.password}`;

  const envConfig = `# 예시
db.host=database.ttalkak.com
db.port=14290
db.name=af36eii8c87
db.username=ae003kdkdfkdie4e
db.password=c240db4e7csmgjd41c78c00`;

  const handleCopy = (
    text: string,
    setCopied: React.Dispatch<React.SetStateAction<boolean>>
  ) => {
    navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="w-full max-w-4xl space-y-6">
      <div className="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700 p-4">
        <div className="flex items-center">
          <FaExclamationTriangle className="mr-2 flex-shrink-0" />
          <span className="font-bold">중요!</span>
        </div>
        <p className="mt-2">
          아래의 설정을 application.properties 파일에 추가하고, 실제 값은 환경
          변수나 외부 설정 파일에서 주입하세요.
        </p>
      </div>

      <div>
        <h4 className="text-lg font-semibold mb-2">
          application.properties 설정:
        </h4>
        <div className="relative overflow-x-auto">
          <CodeBlock code={appPropertiesConfig} />
          <CopyButton
            onClick={() => handleCopy(appPropertiesConfig, setCopiedApp)}
            copied={copiedApp}
          />
        </div>
        {copiedApp && (
          <p className="text-green-600 mt-2">
            application.properties 설정이 클립보드에 복사되었습니다!
          </p>
        )}
      </div>

      <div>
        <h4 className="text-lg font-semibold mb-2">환경 변수 설정 예시:</h4>
        <div className="relative overflow-x-auto">
          <CodeBlock code={envConfig} />
          <CopyButton
            onClick={() => handleCopy(envConfig, setCopiedEnv)}
            copied={copiedEnv}
          />
        </div>
        {copiedEnv && (
          <p className="text-green-600 mt-2">
            환경 변수 설정 예시가 클립보드에 복사되었습니다!
          </p>
        )}
      </div>

      <p>
        이 설정을 사용하면 application.properties 파일 외부에서 위와 같이 값을
        주입할 수 있습니다. 실제 배포 시에는 환경에 맞는 올바른 값으로 변경해야
        합니다.
      </p>
    </div>
  );
};

export default DatabaseConfigGuide;
