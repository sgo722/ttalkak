import { GetDatabaseResponse } from "@/types/database";
import { useQuery, UseQueryResult } from "@tanstack/react-query";
import client from "@/apis//core/client";

const getDatabase = async (
  databaseId: number
): Promise<GetDatabaseResponse> => {
  const response = await client.get<GetDatabaseResponse>({
    url: `/deployment/database/${databaseId}`,
  });
  return response.data;
};

const useGetDatabase = (
  databaseId: number
): UseQueryResult<GetDatabaseResponse, Error> => {
  return useQuery({
    queryKey: ["database", databaseId] as const,
    queryFn: () => getDatabase(databaseId),
  });
};

export default useGetDatabase;
