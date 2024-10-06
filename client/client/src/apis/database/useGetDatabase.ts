import { GetDatabaseResponse } from "@/types/database";
import {
  useSuspenseQuery,
  UseSuspenseQueryResult,
} from "@tanstack/react-query";
import client from "@/apis//core/client";

const getDatabase = async (
  databaseId: number
): Promise<GetDatabaseResponse> => {
  const response = await client.get<GetDatabaseResponse>({
    url: `/deployment/database/${databaseId}`,
  });
  return response.data;
};

const checkDatabaseStatus = (database: GetDatabaseResponse) => {
  return database.status === "PENDING";
};

const useGetDatabase = (
  databaseId: number
): UseSuspenseQueryResult<GetDatabaseResponse, Error> => {
  return useSuspenseQuery({
    queryKey: ["database", databaseId] as const,
    queryFn: () => getDatabase(databaseId),
    refetchInterval: (query) => {
      const database = query.state.data;
      return database && checkDatabaseStatus(database) ? 5000 : false;
    },
  });
};

export default useGetDatabase;
