"use client";
import Image from "next/image";
import { useEffect, useCallback } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import useAuthStore from "@/store/useAuthStore";
import getUserInfo from "@/apis/user/useGetUserInfo";

export default function CallbackPage() {
  const router = useRouter();
  const { setAccessToken, setUserInfo } = useAuthStore();

  const fetchUserInfoAndSetState = useCallback(async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const accessToken = urlParams.get("accessToken");

    if (accessToken) {
      setAccessToken(accessToken);

      try {
        const response = await getUserInfo();
        const userInfo = response.data;
        setUserInfo(userInfo);
        router.push("/");
        toast.success("로그인에 성공했습니다.");
      } catch (error) {
        toast.error("로그인에 실패했습니다.");
        await useAuthStore.getState().logout().catch(console.error);
        router.push("/login");
      }
    } else {
      console.error("url에서 accessToken 찾을 수 없음");
      router.push("/login");
    }
  }, [router, setAccessToken, setUserInfo]);

  useEffect(() => {
    fetchUserInfoAndSetState();
  }, [fetchUserInfoAndSetState]);
  return (
    <div className="flex flex-col items-center justify-center min-h-[calc(100vh-388px)]">
      <div className="animate-jump">
        <Image
          src="/logo.png"
          alt="Logo"
          width={128}
          height={128}
          className="rounded-full"
        />
      </div>
      <h2 className="text-2xl font-bold text-gray-800 mt-8 mb-2">로그인 중</h2>
    </div>
  );
}
