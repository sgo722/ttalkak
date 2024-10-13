import { IoMdClose } from "react-icons/io";
import Image from "next/image";

interface PaymentModalProps {
  formattedText: JSX.Element[];
  isOpen: boolean;
  onClose: () => void;
}

export default function MonitoringModal({
  formattedText,
  isOpen,
  onClose,
}: PaymentModalProps) {
  if (!isOpen) return null;

  const handleBackgroundClick = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();
    onClose();
  };

  const handleContentClick = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 shadow-xl"
      onClick={handleBackgroundClick}
    >
      <div
        className="bg-white shadow-xl relative w-1/2 h-[64%] bg-[#171717] border border-[#ffffff]"
        onClick={handleContentClick}
      >
        <div className="flex items-center justify-between bg-white px-2 py-1.5">
          <div className="flex items-center">
            <Image
              src="/favicon.png"
              alt="favicon"
              width={16}
              height={16}
              className="mr-2 rounded"
            />
            <h2 className="text-black text-xs">AI 분석 결과</h2>
          </div>
          <button onClick={onClose}>
            <IoMdClose size={16} color="#ababab" />
          </button>
        </div>
        <div className="p-4 overflow-y-auto bg-gradient-to-br from-black to-gray-900 text-white h-[calc(100%-28px)]">
          {formattedText}
        </div>
      </div>
    </div>
  );
}
