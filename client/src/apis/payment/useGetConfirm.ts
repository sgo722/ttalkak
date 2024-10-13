import {
  useMutation,
  useQuery,
  useQueryClient,
  UseQueryResult,
} from "@tanstack/react-query";
import { toast } from "react-toastify";
import {
  ConfirmResponse,
  PaymentConfirmProps,
  PaymentCreateProps,
} from "@/types/payment";
import client from "@/apis/core/client";

const getConfrim = async () => {
  const response = await client.get({
    url: "/payment/confirm",
  });

  if (!response.data) {
    throw new Error("Failed to create payment data.");
  }

  return response.data;
};

const useGetConfirm = (): UseQueryResult<ConfirmResponse, Error> => {
  return useQuery({
    queryKey: ["payment", "confirm"] as const,
    queryFn: () => getConfrim(),
  });
};

export default useGetConfirm;
