export enum ServiceType {
  FRONTEND = "FRONTEND",
  BACKEND = "BACKEND",
}

export enum DeployStatus {
  STOPPED = "STOPPED",
  RUNNING = "RUNNING",
  PENDING = "PENDING",
  WAITTING = "WAITTING",
  ERROR = "ERROR",
}

export enum DeployCommand {
  START = "START",
  RESTART = "RESTART",
  STOP = "STOP",
}

export enum DatabaseType {
  MYSQL = "MYSQL",
  MARIADB = "MARIADB",
  MONGODB = "MONGODB",
  POSTGRESQL = "POSTGRESQL",
  REDIS = "REDIS",
}

export enum Framework {
  REACT = "REACT",
  NEXTJS = "NEXTJS",
  SPRINGBOOT = "SPRINGBOOT",
}

export enum BuildTool {
  GRADLE = "gradle",
  MAVEN = "maven",
  CRA = "cra",
  VITE = "vite",
}

export enum PackageManager {
  NPM = "npm",
  YARN = "yarn",
}

export interface Deployment {
  deploymentId: number;
  projectId: number;
  status: DeployStatus;
  serviceType: ServiceType;
  repositoryName: string;
  repositoryUrl: string;
  repositoryLastCommitMessage: string;
  repositoryLastCommitUserProfile: string;
  repositoryLastCommitUserName: string;
  hostingResponses: Hosting[];
}

export interface Hosting {
  hostingId: number;
  detailDomainName: string;
  serviceType: ServiceType;
  hostingPort: number;
}

export interface VersionRequest {
  repositoryLastCommitMessage: string;
  repositoryLastCommitUserProfile: string;
  repositoryLastCommitUserName: string;
}

export interface DatabaseResponse {
  databaseId: number;
  databaseType: string;
  name?: string;
  username: string;
  password: string;
  port: number;
}

export interface DatabaseCreateRequest {
  databaseName: DatabaseType;
  databasePort: number;
  name?: string;
  username: string;
  password: string;
}

export interface GithubRepositoryRequest {
  repositoryOwner: string;
  repositoryName: string;
  repositoryUrl: string;
  rootDirectory: string;
  branch: string;
}

export interface DockerfileCreateRequest {
  exist: boolean;
  buildTool?: BuildTool;
  packageManager?: PackageManager;
  languageVersion: string;
}

export interface EnvVar {
  key: string;
  value: string;
}

export interface DeployData {
  hostingPort: number | null;
  githubRepositoryRequest: GithubRepositoryRequest;
  versionRequest: VersionRequest | null;
  databaseCreateRequests?: DatabaseCreateRequest[] | null;
  dockerfileCreateRequest?: DockerfileCreateRequest;
  envs?: EnvVar[] | null;
  framework: Framework;
}

export interface CreateDeployRequest extends DeployData {
  projectId: number;
  serviceType: ServiceType;
}

export interface GetDeployResponse {
  deploymentId: number;
  projectId: number;
  status: string;
  serviceType: string;
  repositoryName: string;
  repositoryUrl: string;
  branch: string;
  repositoryOwner: string;
  framework: string;
  payloadURL: string;
  versions: null | any[];
  envs: any[];
  hostingResponse: Hosting;
  databaseResponse: DatabaseResponse[];
}
