import { ReactNode } from "react";

export interface SectionProps {
  title: string;
  children: ReactNode;
}

export const Section: React.FC<SectionProps> = ({ title, children }) => (
  <section className="mb-8">
    <h2 className="text-2xl font-bold mb-4">{title}</h2>
    {children}
  </section>
);

export const SubSection: React.FC<SectionProps> = ({ title, children }) => (
  <section className="mb-8">
    <h3 className="text-xl font-bold mb-4">{title}</h3>
    {children}
  </section>
);
