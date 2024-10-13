import { useQuery, UseQueryResult } from "@tanstack/react-query";
import { PaymentResult } from "@/types/payment";
import client from "@/apis/core/client";

const getPaymentResult = async () => {
  const response = await client.get<PaymentResult[]>({
    url: "/payment",
  });

  return response.data;
};

const useGetPaymentResult = (): UseQueryResult<PaymentResult[], Error> => {
  return useQuery({
    queryKey: ["payment", "paymentResult"] as const,
    queryFn: () => getPaymentResult(),
  });
};

export default useGetPaymentResult;
