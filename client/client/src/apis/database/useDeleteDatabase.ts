import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import client from "@/apis/core/client";

const deleteDatabase = async (databaseId: number): Promise<void> => {
  const response = await client.delete({
    url: `/deployment/database/${databaseId}`,
  });

  if (!response.success) {
    throw new Error("데이터베이스를 삭제하지 못했습니다. 서버가 불안정합니다");
  }
};

const useDeleteDatabase = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteDatabase,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["databases"] });
      toast.success("데이터베이스가 성공적으로 삭제되었습니다.");
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });
};

export default useDeleteDatabase;
