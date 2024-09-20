import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { UserInfo } from "@/types/userInfo";

interface AuthState {
  accessToken: string | null; // 액세스 토큰을 저장할 변수
  refreshToken: string | null; // 리프레시 토큰을 저장할 변수
  userInfo: UserInfo | null; // 유저 정보
  isLogin: boolean; // 로그인 여부
  setAccessToken: (token: string | null) => void; // 엑세스 토큰을 설정하는 함수
  setUserInfo: (info: UserInfo | null) => void; // 유저정보를 설정하는 함수
  setIsLogin: (status: boolean) => void; // 로그인 상태를 설정하는 함수
  logout: () => void; // 로그아웃 함수
}

// zustand 스토어 생성
const useAuthStore = create<AuthState>()(
  // 상태를 지속적으로 저장하기 위해 persist 미들웨어 사용
  persist(
    (set) => ({
      // 초기상태와 상태를 변경하는 함수들을 정의
      accessToken: null,
      refreshToken: null,
      userInfo: null,
      isLogin: false,
      setAccessToken: (token) => set({ accessToken: token }),
      setUserInfo: (info) => set({ userInfo: info }),
      setIsLogin: (status) => set({ isLogin: status }),
      logout: () => {
        set({
          accessToken: null,
          userInfo: null,
          isLogin: false,
        });
      },
    }),
    {
      name: "auth-storage",
      storage: createJSONStorage(() => sessionStorage),
      partialize: (state) => ({
        accessToken: state.accessToken,
        userInfo: state.userInfo,
        isLogin: state.isLogin,
      }),
    }
  )
);

export default useAuthStore;
