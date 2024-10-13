import { ReactNode } from "react";

export interface CodeBlockProps {
  code: string;
}

export const CodeBlock: React.FC<CodeBlockProps> = ({ code }) => (
  <pre className="bg-gray-100 p-4 rounded-md overflow-x-auto">
    <code>{code}</code>
  </pre>
);
