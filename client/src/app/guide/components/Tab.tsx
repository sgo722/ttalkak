import { IconType } from "react-icons";

interface TabProps {
  label: string;
  Icon: IconType;
  isActive: boolean;
  onClick: () => void;
}

export const Tab: React.FC<TabProps> = ({ label, Icon, isActive, onClick }) => (
  <button
    className={`flex items-center px-4 py-2 rounded-t-lg transition-colors ${
      isActive
        ? "bg-black text-white"
        : "bg-gray-200 text-gray-700 hover:bg-gray-300"
    }`}
    onClick={onClick}
  >
    <Icon className="mr-2" />
    <span>{label}</span>
  </button>
);
