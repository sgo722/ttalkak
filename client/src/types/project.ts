export interface Project {
  id: number;
  userId: number;
  projectName: string;
  domainName: string;
  expirationDate: string;
  createdAt: string;
  updatedAt: string;
  deployments: null | any;
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

export interface GetProjectsParams {
  page: number;
  size: number;
  sort: string;
  direction: string;
  userId: number;
  searchKeyword?: string;
}

export interface ProjectParams {
  projectName: string;
  domainName: string;
  expirationDate: string;
}

export interface CreateProjectParams extends ProjectParams {}

export interface PatchProjectParams {
  projectId: number;
  data: Partial<ProjectParams>;
}
