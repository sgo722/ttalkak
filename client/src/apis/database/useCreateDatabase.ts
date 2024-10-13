import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { CreateDatabaseRequest } from "@/types/database";
import client from "@/apis/core/client";

const createDatabase = async (data: CreateDatabaseRequest): Promise<void> => {
  const response = await client.post({
    url: "deployment/database",
    data,
  });
  if (!response.success)
    throw new Error("데이터베이스를 생성하지 못했습니다. 서버가 불안정합니다.");
  return response.data;
};

const useCreateDatabase = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createDatabase,
    onSuccess: () => {
      toast.success("데이터베이스가 성공적으로 생성되었습니다.");
      queryClient.invalidateQueries({ queryKey: ["databases"] });
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });
};

export default useCreateDatabase;
