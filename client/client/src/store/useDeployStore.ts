import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import {
  DeployData,
  Framework,
  GithubRepositoryRequest,
  VersionRequest,
  DockerfileCreateRequest,
} from "@/types/deploy";

interface DeployStoreState extends DeployData {
  setFramework: (framework: Framework) => void;
  setGithubRepositoryRequest: (data: Partial<GithubRepositoryRequest>) => void;
  setVersionRequest: (data: Partial<VersionRequest>) => void;
  setHostingPort: (hostingPort: number | null) => void;
  setEnvironment: (env: { key: string; value: string }[] | null) => void;
  setDockerfileCreateRequest: (
    data: Partial<DockerfileCreateRequest> | undefined
  ) => void;
  setFavicon: (favicon: string | null) => void;
  reset: () => void;
}

const initialState: DeployData = {
  framework: Framework.SPRINGBOOT,
  githubRepositoryRequest: {
    repositoryOwner: "",
    repositoryName: "",
    repositoryUrl: "",
    rootDirectory: "./",
    branch: "",
  },
  versionRequest: {
    repositoryLastCommitMessage: "",
    repositoryLastCommitUserProfile: "",
    repositoryLastCommitUserName: "",
  },
  hostingPort: null,
  envs: null,
  dockerfileCreateRequest: {
    exist: true,
    languageVersion: "",
  },
  favicon: null,
};

const useDeployStore = create(
  persist<DeployStoreState>(
    (set) => ({
      ...initialState,
      setFramework: (framework) => set({ framework }),
      setGithubRepositoryRequest: (data) =>
        set((state) => ({
          githubRepositoryRequest: {
            ...state.githubRepositoryRequest,
            ...data,
          },
        })),
      setVersionRequest: (data) =>
        set((state) => ({
          ...state,
          versionRequest: state.versionRequest
            ? { ...state.versionRequest, ...data }
            : (data as VersionRequest),
        })),
      setDockerfileCreateRequest: (
        data: Partial<DockerfileCreateRequest> | undefined
      ) =>
        set((state) => ({
          dockerfileCreateRequest: data
            ? ({
                ...state.dockerfileCreateRequest,
                ...data,
              } as DockerfileCreateRequest)
            : undefined,
        })),
      setHostingPort: (hostingPort) => set({ hostingPort }),
      setEnvironment: (envs) => set({ envs }),
      setFavicon: (favicon: string | null) =>
        set((state) => ({
          ...state,
          favicon: favicon,
        })),
      reset: () => set(initialState),
    }),
    {
      name: "deploy-storage",
      storage: createJSONStorage(() => sessionStorage),
    }
  )
);

export default useDeployStore;
