import { useQuery, UseQueryResult } from "@tanstack/react-query";
import { GetMonitoring } from "@/types/dashboard";
import client from "@/apis/core/client";

const getMonitoring = async (deployId: number): Promise<GetMonitoring> => {
  const response = await client.get<GetMonitoring>({
    url: `log/monitoring/${deployId}`,
  });
  return response.data;
};

const useGetMonitoring = (
  deployId: number,
  enabled: boolean = true
): UseQueryResult<GetMonitoring, Error> => {
  return useQuery({
    queryKey: ["monitoring", deployId] as const,
    queryFn: () => getMonitoring(deployId),
    enabled,
  });
};

export default useGetMonitoring;
