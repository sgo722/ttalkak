import { GetDatabasesResponse } from "@/types/database";
import { useQuery, UseQueryResult } from "@tanstack/react-query";
import client from "@/apis/core/client";

const getDatabases = async (): Promise<GetDatabasesResponse> => {
  const response = await client.get<GetDatabasesResponse>({
    url: "/deployment/database",
  });
  return response.data;
};

const useGetDatabases = (): UseQueryResult<GetDatabasesResponse, Error> => {
  return useQuery({
    queryKey: ["database"],
    queryFn: getDatabases,
  });
};

export default useGetDatabases;
