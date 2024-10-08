import {
  useSuspenseQuery,
  UseSuspenseQueryResult,
} from "@tanstack/react-query";
import { GetProjectsParams, GetProjectsResponse } from "@/types/project";
import client from "@/apis/core/client";

const getProjects = async (
  params: GetProjectsParams
): Promise<GetProjectsResponse> => {
  const response = await client.get<GetProjectsResponse>({
    url: "/project/search",
    params,
  });

  console.log("useGetProjects");

  const { content, totalPages, totalElements } = response.data;
  return response.data;
};

const useGetProjects = (
  params: GetProjectsParams
): UseSuspenseQueryResult<GetProjectsResponse, Error> => {
  return useSuspenseQuery({
    queryKey: ["projects", params] as const,
    queryFn: () => getProjects(params),
  });
};

export default useGetProjects;
