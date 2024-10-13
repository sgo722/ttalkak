import React from "react";

export interface TermsSectionProps {
  title: string;
  content: string[];
}

export default function TermsSection({ title, content }: TermsSectionProps) {
  return (
    <section className="terms-section border-b border-gray-200 py-4">
      <h2 className="terms-title text-xl font-semibold mb-3">{title}</h2>
      <div className="terms-content">
        {content.map((paragraph, index) => (
          <p key={index} className="terms-paragraph mb-2">
            {paragraph}
          </p>
        ))}
      </div>
    </section>
  );
}
