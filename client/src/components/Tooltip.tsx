import Tippy from "@tippyjs/react";
import "tippy.js/dist/tippy.css";
import { IoMdInformationCircleOutline } from "react-icons/io";

interface TooltipProps {
  content: string;
  iconClassName?: string;
}

export default function Tooltip({ content, iconClassName }: TooltipProps) {
  return (
    <Tippy content={<span>{content}</span>}>
      <span className="cursor-help ml-1">
        <IoMdInformationCircleOutline
          className={`text-gray-500 ${iconClassName}`}
        />
      </span>
    </Tippy>
  );
}
