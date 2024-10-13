import { useMutation } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { DeployCommand } from "@/types/deploy";
import client from "@/apis/core/client";

interface ModifyDatabaseStatusRequest {
  databaseId: number;
  command: DeployCommand;
}

const modifyDatabaseStatus = async (
  data: ModifyDatabaseStatusRequest
): Promise<void> => {
  const response = await client.post({
    url: "/deployment/database/command",
    data,
  });
  if (!response.success) {
    throw new Error("요청에 실패했습니다. 서버가 불안정합니다.");
  }
};

const useModifyDatabaseStatus = () => {
  return useMutation({
    mutationFn: modifyDatabaseStatus,
    onSuccess: () => {
      toast.success("요청에 성공했습니다.");
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });
};

export default useModifyDatabaseStatus;
