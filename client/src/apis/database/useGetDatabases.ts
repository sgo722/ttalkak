import { GetDatabasesResponse, GetDatabasesParams } from "@/types/database";
import { useQuery, UseQueryResult } from "@tanstack/react-query";
import client from "@/apis/core/client";

const getDatabases = async (
  params: GetDatabasesParams
): Promise<GetDatabasesResponse> => {
  const response = await client.get<GetDatabasesResponse>({
    url: "/deployment/database/search",
    params,
  });
  return response.data;
};

const useGetDatabases = (
  params: GetDatabasesParams
): UseQueryResult<GetDatabasesResponse, Error> => {
  return useQuery({
    queryKey: ["databases", params] as const,
    queryFn: () => getDatabases(params),
    throwOnError: true,
  });
};

export default useGetDatabases;
