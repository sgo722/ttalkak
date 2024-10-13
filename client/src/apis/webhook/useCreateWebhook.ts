import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import webhookClient from "@/apis/core/webhookClient";

interface CreateWebhookRequest {
  owner: string;
  repo: string;
  webhookUrl: string;
}

const createWebhook = async ({
  owner,
  repo,
  webhookUrl,
}: CreateWebhookRequest) => {
  try {
    const response = await webhookClient.repos.createWebhook({
      owner,
      repo,
      config: {
        url: webhookUrl,
        content_type: "json",
      },
      events: ["push"],
    });
    return response.data;
  } catch (error: any) {
    if (error.message && typeof error.message === "string") {
      if (error.message.includes("cannot have more than 20 hooks")) {
        throw new Error(
          "웹훅 생성 실패 : 20개이상의 웹훅이 등록되어 있습니다."
        );
      }
    }
    throw new Error("웹훅 생성에 실패했습니다.");
  }
};

const useCreateWebhook = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createWebhook,
    onSuccess: (_, variables) => {
      toast.success("웹훅이 성공적으로 생성되었습니다.");
      queryClient.invalidateQueries({
        queryKey: ["webhooks", variables.owner, variables.repo],
      });
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });
};

export default useCreateWebhook;
