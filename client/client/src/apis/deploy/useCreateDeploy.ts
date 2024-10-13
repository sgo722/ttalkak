import {
  useMutation,
  UseMutationResult,
  useQueryClient,
} from "@tanstack/react-query";
import { toast } from "react-toastify";
import { CreateDeployRequest } from "@/types/deploy";
import client from "@/apis/core/client";

interface CreateDeployResponse {
  webhookUrl: string;
}

const createDeploy = async (
  data: CreateDeployRequest
): Promise<CreateDeployResponse> => {
  const response = await client.post({
    url: "deployment",
    data,
  });
  if (!response.success)
    throw new Error("프로젝트를 생성하지 못했습니다. 서버가 불안정합니다.");
  return response.data;
};

const useCreateDeploy = (): UseMutationResult<
  CreateDeployResponse,
  Error,
  CreateDeployRequest,
  unknown
> => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createDeploy,
    onSuccess: () => {
      toast.success("배포가 성공적으로 생성되었습니다.");
      // 프로젝트 목록 쿼리 무효화
      queryClient.invalidateQueries({ queryKey: ["project"] });
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });
};

export default useCreateDeploy;
