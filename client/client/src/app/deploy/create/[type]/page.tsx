import { Suspense } from "react";
import GitHubRepos from "@/app/deploy/create/[type]/components/githubRepos";
import SkeletonUI from "@/app/deploy/create/[type]/components/SkeletonUI";

export default function GitHubRepositoriesPage() {
  return (
    <Suspense fallback={<SkeletonUI />}>
      <GitHubRepos />
    </Suspense>
  );
}
