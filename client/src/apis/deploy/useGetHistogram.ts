import { useQuery, UseQueryResult } from "@tanstack/react-query";
import { HistogramParams, HistogramResponse } from "@/types/dashboard";
import client from "@/apis/core/client";

const getHistogram = async (
  params: HistogramParams
): Promise<HistogramResponse> => {
  const response = await client.get<HistogramResponse>({
    url: "/log/histogram",
    params,
  });

  console.log("히스토그램 params", params);

  return response.data;
};

const useGetHistogram = (
  params: HistogramParams,
  enabled: boolean = true
): UseQueryResult<HistogramResponse, Error> => {
  return useQuery({
    queryKey: ["histogram", params] as const,
    queryFn: () => getHistogram(params),
    enabled,
  });
};

export default useGetHistogram;
