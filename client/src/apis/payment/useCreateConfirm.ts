import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { PaymentConfirmProps, PaymentCreateProps } from "@/types/payment";
import client from "@/apis/core/client";

const createConfrim = async (data: PaymentConfirmProps) => {
  const response = await client.post({
    url: "/payment/confirm",
    data,
  });

  if (!response.data) {
    throw new Error("Failed to create payment data.");
  }

  return response.data;
};

const useCreateConfirm = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createConfrim,
    onSuccess: () => {
      toast.success("결제 승인 등록이 완료되었습니다.");
      queryClient.invalidateQueries({
        queryKey: ["payment", "createConfirm"] as const,
      });
    },
    onError: (error) => {
      toast.error(`Failed to create payment: ${error.message}`);
    },
  });
};

export default useCreateConfirm;
