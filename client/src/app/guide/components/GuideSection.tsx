import React, { ReactNode } from "react";

export interface GuideSectionProps {
  title: string;
  children: ReactNode;
  icon?: ReactNode;
}

export default function GuideSection({
  title,
  icon,
  children,
}: GuideSectionProps) {
  return (
    <div className="mb-8 border-b pb-6 last:border-b-0">
      <h2 className="text-3xl font-semibold mb-4 flex items-center">
        {icon && <span className="text-blue-500 mr-3">{icon}</span>}
        {title}
      </h2>
      {children}
    </div>
  );
}
