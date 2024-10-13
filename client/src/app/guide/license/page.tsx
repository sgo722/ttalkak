"use client";
import React, { useState } from "react";
import {
  RiArrowDropDownLine,
  RiArrowDropUpLine,
  RiFileTextLine,
} from "react-icons/ri";
import { TtalkakPolicyContent } from "./constants/TtalkakPolicyContent";

interface TermsSectionProps {
  title: string;
  content: string[];
}

interface GuideSectionProps {
  title: string;
  icon: React.ReactElement;
  children: React.ReactNode;
}

const GuideSection: React.FC<GuideSectionProps> = ({
  title,
  icon,
  children,
}) => (
  <section className="mb-8">
    <h2 className="text-2xl font-semibold mb-4 flex items-center">
      {icon}
      <span className="ml-2">{title}</span>
    </h2>
    {children}
  </section>
);

const TermsSection: React.FC<TermsSectionProps> = ({ title, content }) => {
  const [isOpen, setIsOpen] = useState<boolean>(false);

  return (
    <div>
      <div className="mb-6 w-full bg-white rounded-lg overflow-hidden hover:shadow-lg transition-shadow">
        <button
          className="flex items-center p-4 w-full text-left border rounded-lg"
          onClick={() => setIsOpen(!isOpen)}
        >
          <RiFileTextLine size={20} className="mr-3 text-blue-500" />
          <h3 className="text-lg font-semibold flex-grow">{title}</h3>
          {isOpen ? (
            <RiArrowDropUpLine size={24} className="text-gray-500" />
          ) : (
            <RiArrowDropDownLine size={24} className="text-gray-500" />
          )}
        </button>
        {isOpen && (
          <div className="p-4 text-gray-600">
            {content.map((item, index) => (
              <p key={index} className="mb-3 last:mb-0">
                {item}
              </p>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

const TTALKAKTermsOfServiceAgreement: React.FC = () => {
  return (
    <div className="container min-h-screen max-h-screen overflow-y-auto w-full px-10">
      <div className="flex-grow sm:px-6 lg:px-8 pt-16 grid px-4">
        <h1 className="text-4xl font-bold mb-8 text-center">
          TTALKAK 서비스 이용약관 및 라이선스
        </h1>
      </div>

      <div className="space-y-6">
        {TtalkakPolicyContent.map(
          (section: TermsSectionProps, index: number) => (
            <TermsSection
              key={index}
              title={section.title}
              content={section.content}
            />
          )
        )}
      </div>
      <div className="h-20"></div>
    </div>
  );
};

export default TTALKAKTermsOfServiceAgreement;
