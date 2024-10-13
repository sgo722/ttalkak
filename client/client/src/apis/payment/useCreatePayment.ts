import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { PaymentCreateProps } from "@/types/payment";
import client from "@/apis/core/client";

const createPayment = async (data: PaymentCreateProps) => {
  const response = await client.post({
    url: "/payment/signature",
    data,
  });

  if (!response.data) {
    throw new Error("Failed to create payment data.");
  }

  return response.data;
};

const useCreatePayment = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createPayment,
    onSuccess: () => {
      toast.success("지갑 등록이 완료되었습니다.");

      queryClient.invalidateQueries({ queryKey: ["payment"] });
    },
    onError: (error) => {
      toast.error(`Failed to create payment: ${error.message}`);
    },
  });
};

export default useCreatePayment;
