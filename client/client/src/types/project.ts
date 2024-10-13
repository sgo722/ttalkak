import { StatusMessage } from "@/types/deploy";

export interface Deployment {
  deploymentId: number;
  projectId: number;
  repositoryLastCommitMessage: string;
  repositoryLastCommitUserName: string;
  repositoryLastCommitUserProfile: string;
  repositoryName: string;
  repositoryOwner: string;
  repositoryUrl: string;
  serviceType: string;
  status: string;
  statusMessage: StatusMessage;
}

export interface Project {
  id: number;
  userId: number;
  projectName: string;
  domainName: string;
  webhookToken: string;
  expirationDate: string;
  createdAt: string;
  updatedAt: string;
  favicon: string | null;
  deployments: Deployment[];
}

export enum PaymentType {
  FixedTerm = "fixed-term",
  Unlimited = "unlimited",
}

export interface ProjectFormData {
  projectName: string;
  domainName: string;
  paymentType: PaymentType;
  expirationDate: string;
}

export interface GetProjectsResponse {
  content: Project[];
  totalPages: number;
  totalElements: number;
}

export interface GetProjectsRequest {
  page: number;
  size: number;
  sort: string;
  direction: string;
  userId?: number;
  searchKeyword?: string;
}

export interface ProjectRequest {
  projectName: string;
  domainName: string;
  expirationDate: string;
}

export interface CreateProjectRequest extends ProjectRequest {}

export interface ModifyProjectRequest {
  projectId: number;
  data: Partial<ProjectRequest>;
}
