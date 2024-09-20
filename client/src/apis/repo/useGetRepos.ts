import { useQuery } from "@tanstack/react-query";
import githubClient from "@/apis/core/githubClient";
import { Repository } from "@/types/repo";
import useAuthStore from "@/store/useAuthStore";
// Promise<Repository[]>를 반환한다고 명시적으로 타입 지정
// Repository 객체의 배열을 포함하는 promise반환한다는 뜻

const getRepos = async (githubApiKey: string): Promise<Repository[]> => {
  //응답 데이터가 Repository 객체의 배열이라고 명시
  const client = githubClient(githubApiKey);
  const response = await client.get<Repository[]>("/user/repos", {
    params: { type: "public", per_page: 100 },
  });
  return response.data;
};

const useGetRepos = () => {
  const githubApiKey = useAuthStore.getState().userInfo?.accessToken;

  return useQuery({
    queryKey: ["repos", githubApiKey] as const,
    queryFn: () => getRepos(githubApiKey || ""),
    enabled: !!githubApiKey, // API 키가 있을 때만 쿼리 실행
  });
};

export default useGetRepos;
