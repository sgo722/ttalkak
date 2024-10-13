import { texts } from "@/app/components/VideoPage";

export default function NavigationMenu({
  currentSlide,
  setCurrentSlide,
}: {
  currentSlide: number;
  setCurrentSlide: (slide: number) => void;
}) {
  return (
    <div className="fixed right-10 top-1/2 transform -translate-y-1/2 z-50">
      <ul className="space-y-4">
        {texts.map((text, index) => (
          <li key={index} className="flex items-center justify-end">
            <button
              onClick={() => setCurrentSlide(index + 1)}
              className={`text-right pr-3 py-2 transition-all duration-300 flex items-center ${
                currentSlide === index + 1
                  ? "text-white font-bold"
                  : "text-gray-400 hover:text-gray-200"
              }`}
            >
              <span className="mr-3">{text.title}</span>
              <span
                className={`w-2 rounded-full ${
                  currentSlide === index + 1
                    ? "bg-white border-white h-5"
                    : "bg-gray-400 h-2"
                }`}
              ></span>
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
