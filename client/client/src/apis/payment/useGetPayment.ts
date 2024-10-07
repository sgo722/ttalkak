import { useQuery, UseQueryResult } from "@tanstack/react-query";
import { PaymentResponse } from "@/types/payment";
import client from "@/apis/core/client";

const getPayment = async (range: number): Promise<PaymentResponse> => {
  const response = await client.get<PaymentResponse>({
    url: "/payment",
    params: { range },
  });

  return response.data;
};

const useGetPayment = (
  range: number
): UseQueryResult<PaymentResponse, Error> => {
  return useQuery({
    queryKey: ["payment", range] as const,
    queryFn: () => getPayment(range),
  });
};

export default useGetPayment;
