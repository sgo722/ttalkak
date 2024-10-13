import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { UserInfo, Validation } from "@/types/userInfo";
import Cookies from "js-cookie";

interface AuthState {
  accessToken: string | null;
  userInfo: UserInfo | null;
  isValidated: boolean;
  isLogin: boolean;
  setAccessToken: (token: string | null) => void;
  setUserInfo: (info: UserInfo | null) => void;
  setValidation: (validation: Validation) => void;
  logout: () => Promise<boolean>;
}

// zustand 스토어 생성
const useAuthStore = create<AuthState>()(
  // 상태를 지속적으로 저장하기 위해 persist 미들웨어 사용
  persist(
    (set) => ({
      // 초기상태와 상태를 변경하는 함수들을 정의
      accessToken: null,
      userInfo: null,
      isLogin: false,
      isValidated: false,
      setAccessToken: (token) => {
        set({ accessToken: token, isLogin: !!token });
        if (token) {
          Cookies.set("isLogin", "true", { sameSite: "strict" });
        } else {
          Cookies.remove("isLogin");
        }
      },
      setUserInfo: (info) => set({ userInfo: info }),
      setValidation: (validation) => {
        const isValidated = Object.values(validation).every(Boolean);
        set({ isValidated });
      },
      logout: async () => {
        try {
          set({
            accessToken: null,
            userInfo: null,
            isValidated: false,
            isLogin: false,
          });
          Cookies.remove("isLogin");
          return true;
        } catch (error) {
          return false;
        }
      },
    }),
    {
      name: "auth-storage",
      storage: createJSONStorage(() => sessionStorage),
      partialize: (state) => ({
        accessToken: state.accessToken,
        isLogin: state.isLogin,
        userInfo: state.userInfo,
        validation: state.isValidated,
      }),
    }
  )
);

export default useAuthStore;
