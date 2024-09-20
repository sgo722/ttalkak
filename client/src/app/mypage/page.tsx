"use client";

import React, { useState } from "react";
import EmailRegistrationModal from "@/components/EmailRegistrationModal";

export default function MyPage() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div>
      <h1>마이페이지</h1>
      <button onClick={openModal}>등록하기</button>
      <EmailRegistrationModal isOpen={isModalOpen} onClose={closeModal} />
    </div>
  );
}
