"use client";

import { useEffect, useState } from "react";
import useGetPayment from "@/apis/payment/useGetPayment";

export default function PaymentPage() {
  const { data: paymentData } = useGetPayment(Number(7));

  useEffect(() => {
    if (paymentData) {
      console.log(paymentData);
    }
  }, [paymentData]);

  return <div>결제내역</div>;
}
