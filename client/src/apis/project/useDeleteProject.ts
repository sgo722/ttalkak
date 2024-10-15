import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import client from "@/apis/core/client";

const deleteProject = async (projectId: string): Promise<void> => {
  const response = await client.delete({
    url: `/project/${projectId}`,
  });

  if (!response.success) {
    throw new Error(
      "프로젝트를 삭제하지 못했습니다. 서버 상태가 불안정합니다."
    );
  }
};

const useDeleteProject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteProject,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["projects"] });
      toast.success("프로젝트가 성공적으로 삭제되었습니다.");
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });
};

export default useDeleteProject;
