import { useState, useEffect } from "react";

const useDebounce = (input: string, delay: number) => {
  const [debouncedInput, setDebouncedInput] = useState<string>(input);

  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedInput(input);
    }, delay);

    return () => {
      clearTimeout(timer);
    };
  }, [input, delay]);

  return debouncedInput;
};

export default useDebounce;
