"use client";

import React, { ReactNode } from "react";
import ReactQueryProvider from "@/providers/ReactQueryProvider";
import { ToastContainer, ToastContainerProps } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

interface ClientWrapperProps {
  children: ReactNode;
}

export default function ClientWrapper({
  children,
}: ClientWrapperProps): React.ReactElement {
  const toastConfig: ToastContainerProps = {
    position: "bottom-right",
    autoClose: 3000,
    hideProgressBar: false,
    newestOnTop: false,
    closeOnClick: true,
    rtl: false,
    pauseOnFocusLoss: true,
    draggable: true,
    pauseOnHover: true,
    theme: "light",
  };

  return (
    <ReactQueryProvider>
      {children}
      <ToastContainer {...toastConfig} />
    </ReactQueryProvider>
  );
}
