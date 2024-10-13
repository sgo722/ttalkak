import { GetDatabasesResponse, GetDatabasesRequest } from "@/types/database";
import {
  useSuspenseQuery,
  UseSuspenseQueryResult,
} from "@tanstack/react-query";
import client from "@/apis/core/client";

const getDatabases = async (
  params: GetDatabasesRequest
): Promise<GetDatabasesResponse> => {
  const response = await client.get<GetDatabasesResponse>({
    url: "/deployment/database/search",
    params,
  });
  return response.data;
};

const useGetDatabases = (
  params: GetDatabasesRequest
): UseSuspenseQueryResult<GetDatabasesResponse, Error> => {
  return useSuspenseQuery({
    queryKey: ["databases", params] as const,
    queryFn: () => getDatabases(params),
  });
};

export default useGetDatabases;
