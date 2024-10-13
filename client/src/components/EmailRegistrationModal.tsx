"use client";

import { useState, ChangeEvent, useEffect, FormEvent } from "react";
import client from "@/apis/core/client";
import { IoMdClose } from "react-icons/io";
import useAuthStore from "@/store/useAuthStore";
import { UserEmailForm } from "@/types/userInfo";
import useCreateEmail from "@/apis/user/useCreateEmail";

interface EmailRegistrationModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function EmailRegistrationModal({
  isOpen,
  onClose,
}: EmailRegistrationModalProps) {
  const { userInfo, setUserInfo } = useAuthStore();
  const [email, setEmail] = useState("");
  const [isSent, setIsSent] = useState(false); // 인증 메일 발송 여부
  const [verificationCode, setVerificationCode] = useState("");
  const [timeLeft, setTimeLeft] = useState(300);

  const { mutate: createEmail } = useCreateEmail();

  // 만료 시간 관리
  useEffect(() => {
    if (isSent && timeLeft > 0) {
      const timer = setInterval(() => {
        setTimeLeft((prevTimeLeft) => prevTimeLeft - 1);
      }, 1000);
      return () => clearInterval(timer);
    } else if (timeLeft === 0) {
      setIsSent(false);
    }
  }, [isSent, timeLeft]);

  const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handleCodeChange = (e: ChangeEvent<HTMLInputElement>) => {
    setVerificationCode(e.target.value);
  };

  // 인증 메일 발송 요청
  const sendVerificationEmail = async () => {
    if (!email) return;

    try {
      const response = await client.post({
        url: "notification/email/code",
        data: { email, nickname: userInfo?.username },
      });

      if (!response.success) {
        throw new Error(response.message || "인증 메일 전송 실패");
      }

      setIsSent(true);
      setTimeLeft(300);
    } catch (error) {
      console.error("Email sending failed:", error);
    }
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!userInfo || !userInfo.userId) {
      throw new Error("유저 정보가 없습니다.");
    }

    const emailData: UserEmailForm = {
      userId: userInfo.userId,
      email,
      code: verificationCode,
    };

    createEmail(emailData, {
      onSuccess: () => {
        const updatedUserInfo = {
          ...userInfo,
          email: email,
          emailVerified: true,
        };

        setUserInfo(updatedUserInfo);
        onClose();
      },
      onError: (error) => {
        console.error("Email verification failed:", error);
      },
    });
  };

  const handleCloseModal = () => {
    setEmail("");
    setIsSent(false);
    setVerificationCode("");
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-xl relative w-96">
        <button className="absolute top-4 right-4" onClick={handleCloseModal}>
          <IoMdClose />
        </button>

        <h2 className="text-center text-xl mb-6">이메일 등록</h2>

        <form className="space-y-4" onSubmit={handleSubmit}>
          <div className="flex items-center space-x-2">
            <input
              type="email"
              value={email}
              onChange={handleEmailChange}
              placeholder="example@gmail.com"
              disabled={isSent}
              className="border border-gray-300 px-4 py-2 w-full rounded-md"
            />
            <button
              type="button"
              onClick={sendVerificationEmail}
              disabled={isSent}
              className={`py-2 w-32 rounded-md flex justify-center ${
                isSent
                  ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                  : "bg-gray-200 text-black cursor-pointer"
              }`}
            >
              {isSent ? "전송 완료" : "인증하기"}
            </button>
          </div>

          <input
            type="text"
            value={verificationCode}
            onChange={handleCodeChange}
            placeholder="인증번호 6자리를 입력해주세요"
            className="border border-gray-300 px-4 py-2 w-full rounded-md"
          />

          <button
            type="submit"
            className="bg-black text-white py-2 flex w-full justify-center rounded-md cursor-pointer"
          >
            완료하기
          </button>

          {isSent ? (
            <div className="text-gray-500 text-center text-red-400 text-sm">
              {Math.floor(timeLeft / 60)}분 {timeLeft % 60}초
            </div>
          ) : null}
        </form>
      </div>
    </div>
  );
}
