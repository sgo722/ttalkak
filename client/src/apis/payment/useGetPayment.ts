import { useQuery, UseQueryResult } from "@tanstack/react-query";
import { PaymentRegistrationStatus } from "@/types/payment";
import client from "@/apis/core/client";

const getPayment = async (): Promise<PaymentRegistrationStatus> => {
  const response = await client.get<PaymentRegistrationStatus>({
    url: "/payment/signature",
  });

  return response.data;
};

const useGetPayment = (): UseQueryResult<PaymentRegistrationStatus, Error> => {
  return useQuery({
    queryKey: ["payment", "signature"] as const,
    queryFn: () => getPayment(),
  });
};

export default useGetPayment;
