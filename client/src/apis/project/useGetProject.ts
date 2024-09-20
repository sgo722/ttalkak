import client from "@/apis/core/client";
import {
  useSuspenseQuery,
  UseSuspenseQueryResult,
} from "@tanstack/react-query";
import { Project } from "@/types/project";

const getProject = async (projectId: number): Promise<Project> => {
  const response = await client.get<Project>({
    url: `/project/${projectId}`,
  });
  return response.data;
};

const useGetProject = (
  projectId: number
): UseSuspenseQueryResult<Project, Error> => {
  return useSuspenseQuery({
    queryKey: ["project", projectId] as const,
    queryFn: () => getProject(projectId),
  });
};

export default useGetProject;
