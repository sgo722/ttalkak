import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import client from "@/apis/core/client";

const deleteDeploy = async (deploymentId: number): Promise<void> => {
  const response = await client.delete({
    url: `/deployment/${deploymentId}`,
  });

  if (!response.success) {
    throw new Error(
      "프로젝트를 삭제하지 못했습니다. 서버 상태가 불안정합니다."
    );
  }
};

const useDeleteDeploy = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteDeploy,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["project"] });
      toast.success("성공적으로 삭제되었습니다.");
    },
    onError: (error) => {
      toast.success(error.message);
    },
  });
};

export default useDeleteDeploy;
