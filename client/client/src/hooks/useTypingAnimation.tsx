import { useState, useEffect } from "react";

const useTypingAnimation = (text: string, speed: number = 50) => {
  const [displayedText, setDisplayedText] = useState<string>("");
  const [isCursorVisible, setIsCursorVisible] = useState<boolean>(true);

  useEffect(() => {
    setDisplayedText("");

    let currentText = "";
    let index = 0;

    const textArray = Array.from(text);

    if (text) {
      const intervalId = setInterval(() => {
        if (index < textArray.length) {
          currentText += textArray[index];
          setDisplayedText(currentText);
          index += 1;
        } else {
          clearInterval(intervalId);
          setIsCursorVisible(false);
        }
      }, speed);

      return () => clearInterval(intervalId);
    }
  }, [text]);

  useEffect(() => {
    const cursorIntervalId = setInterval(() => {
      setIsCursorVisible((prev) => !prev);
    }, 300);

    return () => clearInterval(cursorIntervalId);
  }, []);

  return `${displayedText}${isCursorVisible && displayedText.length < text.length ? "|" : ""}`;
};

export default useTypingAnimation;
