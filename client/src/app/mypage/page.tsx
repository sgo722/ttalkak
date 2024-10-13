"use client";

import Image from "next/image";
import React, { useState } from "react";
import PaymentModal from "@/components/PaymentModal";
import EmailRegistrationModal from "@/components/EmailRegistrationModal";
import useAuthStore from "@/store/useAuthStore";
import useGetConfirm from "@/apis/payment/useGetConfirm";
import useGetPayment from "@/apis/payment/useGetPayment";
import useGetPaymentResult from "@/apis/payment/useGetPaymentResult";
import { TbCoinFilled } from "react-icons/tb";

export default function MyPage() {
  const { userInfo } = useAuthStore();
  const [isEmailModalOpen, setIsEmailModalOpen] = useState(false);
  const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false);

  const { data: confirmData } = useGetConfirm();
  const { data: paymentData, isLoading: isDataLoading } = useGetPayment();
  const { data: paymentResult, isLoading: isResultLoading } =
    useGetPaymentResult();

  const openEmailModal = () => {
    setIsEmailModalOpen(true);
  };

  const closeEmailModal = () => {
    setIsEmailModalOpen(false);
  };

  const openPaymentModal = () => {
    setIsPaymentModalOpen(true);
  };

  const closePaymentModal = () => {
    setIsPaymentModalOpen(false);
  };

  const profile = "rounded-full w-16 h-16 mr-12 bg-gray-200";
  const thStyle = "font-normal w-20 text-left text-gray-400";

  return (
    <>
      <div className="w-full border rounded p-6 hover:shadow-lg transition-shadow duration-200">
        <h2 className="font-semibold text-lg mb-3.5">내 정보</h2>
        <div className="flex">
          {userInfo ? (
            <Image
              src={userInfo.profileImage}
              width={64}
              height={64}
              alt="profile_img"
              className={profile}
            />
          ) : (
            <div className={profile}></div>
          )}
          <div className="flex flex-col justify-around">
            <div className="flex items-center">
              <div className={thStyle}>지갑 주소</div>
              {isDataLoading ? (
                <div className="text-[#cbcbcb]">데이터를 불러오는 중입니다</div>
              ) : paymentData?.hasKey &&
                confirmData?.contract &&
                confirmData?.admin ? (
                <div>{paymentData.address}</div>
              ) : (
                <div className="flex items-center">
                  <div className="text-gray-700 text-sm min-w-44">
                    등록된 지갑이 없습니다.
                  </div>
                  <button
                    className="bg-black text-white rounded px-3.5 py-0.5 text-sm ml-6"
                    onClick={openPaymentModal}
                  >
                    등록하기
                  </button>
                </div>
              )}
            </div>
            <div className="flex items-center">
              <div className={thStyle}>이메일</div>
              {userInfo && userInfo.emailVerified ? (
                <div>{userInfo.email}</div>
              ) : (
                <div className="flex items-center">
                  <div className="text-gray-700 text-sm min-w-44">
                    등록된 이메일이 없습니다.
                  </div>
                  <button
                    className="bg-black text-white rounded px-3.5 py-0.5 text-sm ml-6"
                    onClick={openEmailModal}
                  >
                    등록하기
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
      <div className="w-full border rounded p-6 mt-8 min-h-[340px] hover:shadow-lg transition-shadow duration-200">
        <div className="flex justify-between font-semibold text-lg mb-3.5 pb-2 border-b border-[#a7a7a7]">
          <div>청구 금액</div>
          <div>
            <span className="mr-4">합계</span>
            {paymentResult &&
              paymentResult
                .reduce((acc, payment) => acc + parseFloat(payment.amount), 0)
                .toFixed(1)}{" "}
            ssf
          </div>
        </div>
        {isResultLoading ? (
          <div className="text-gray-500 text-center mt-32">
            데이터를 불러오는 중입니다
          </div>
        ) : paymentResult && paymentResult.length > 0 ? (
          <div className="max-h-[300px] min-h-[220px] overflow-y-auto custom-scrollbar">
            {paymentResult.map((payment) => (
              <div
                className="flex justify-between py-1.5 px-3"
                key={payment.serviceId}
              >
                <div className="flex">
                  <div className="w-60">{payment.domain}</div>
                  <div className="w-40">{payment.serviceType}</div>
                </div>
                <div className="flex items-center justify-end w-24">
                  <TbCoinFilled className="mr-1.5" color="#bdbdbd" />
                  {payment.amount}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="text-gray-500 text-center mt-32">
            결제 내역이 없습니다.
          </div>
        )}
      </div>

      <PaymentModal isOpen={isPaymentModalOpen} onClose={closePaymentModal} />
      <EmailRegistrationModal
        isOpen={isEmailModalOpen}
        onClose={closeEmailModal}
      />
    </>
  );
}
