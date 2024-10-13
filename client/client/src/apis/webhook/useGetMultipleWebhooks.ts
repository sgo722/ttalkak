import { useQueries } from "@tanstack/react-query";
import { getWebhooks } from "@/apis/webhook/useGetWebhooks";

interface GetMultipleWebhooksRequest {
  owner: string;
  repo: string;
}

const useGetMultipleWebhooks = (
  repos: GetMultipleWebhooksRequest[],
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
