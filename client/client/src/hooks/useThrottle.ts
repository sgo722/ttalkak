import { useRef, useCallback } from "react";

type AnyFunction = (...args: any[]) => any;
// (...args:any[]) 어떤 타입의 인자든, 몇개든 받을 수 있음
// => any 함수가 어떤 타입의 값이든 반환할 수 있음

const useThrottle = <T extends AnyFunction>(callback: T, delay: number): T => {
  // <T extends AnyFunction> AnyFunction을 확장한다는 의미
  // callback:T 콜백함수의 타입은 T(AnyFunction을 확장한거) 어떤 함수타입이든 될 수 있음
  // :T 반환타입도 T(AnyFunction을 확장한거) 입력으로 받은 함수와 동일한 타입의 함수를 반환
  const lastRun = useRef(Date.now());

  return useCallback(
    (...args: Parameters<T>) => {
      const now = Date.now();
      if (now - lastRun.current >= delay) {
        lastRun.current = now;
        return callback(...args);
      }
    },
    [callback, delay]
  ) as T;
};

export default useThrottle;
