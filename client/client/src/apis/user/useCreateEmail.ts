import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { UserEmailForm } from "@/types/userInfo";
import client from "@/apis/core/client";

const createEmail = async (data: UserEmailForm) => {
  const response = await client.post({
    url: "/notification/email/confirm",
    data,
  });

  return response.data;
};

const useCreateEmail = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createEmail,
    onSuccess: () => {
      toast.success("이메일 등록이 완료되었습니다.");

      queryClient.invalidateQueries({ queryKey: ["email"] });
    },
    onError: (error) => {
      toast.error(`Failed to create email: ${error.message}`);
    },
  });
};

export default useCreateEmail;
