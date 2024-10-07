import { useQueries } from "@tanstack/react-query";
import { getWebhooks } from "@/apis/webhook/useGetWebhooks";

interface GetMultipleWebhooksParams {
  owner: string;
  repo: string;
}

const useGetMultipleWebhooks = (
  repos: GetMultipleWebhooksParams[],
  isEnabled: boolean
) => {
  return useQueries({
    queries: repos.map((repo) => ({
      queryKey: ["webhooks", repo.owner, repo.repo],
      queryFn: () => getWebhooks(repo),
      enabled: isEnabled,
    })),
  });
};

export default useGetMultipleWebhooks;
