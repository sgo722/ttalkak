import client from "@/apis/core/client";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { DeployCommand } from "@/types/deploy";

interface DeployStatusRequest {
  deploymentId: string;
  command: DeployCommand;
}

const modifyDeployStatus = async (data: DeployStatusRequest): Promise<void> => {
  const response = await client.post({
    url: "/deployment/command",
    data,
  });
  if (!response.success) {
    throw new Error(response.message!!);
  }
};

const useModifyDeployStatus = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: modifyDeployStatus,
    onSuccess: () => {
      toast.success("요청에 성공했습니다.");
      // 쿼리무효화 추가해야함
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });
};

export default useModifyDeployStatus;
