type CountInfo<field extends string> = {
  [key in field]: string;
} & {
  count: number;
};

export type AccessIp = CountInfo<"ip">;
export type UseMethod = CountInfo<"method">;
export type TopPath = CountInfo<"path">;

export interface ErrorCategory {
  category: string;
  count: number;
  topPaths: TopPath[];
}

export interface MonitoringInfo {
  totalDocCount: number;
  totalErrors: number;
  avgResponseTime: number;
  accessIpInfos: AccessIp[];
  usedMethodInfos: UseMethod[];
  errorCategories: ErrorCategory;
}

export interface GetMonitoring {
  monitoringInfoResponse: MonitoringInfo;
  answer: string;
}

export interface HistogramParams {
  from: string;
  to: string;
  deploymentId: number;
}

export interface Histogram {
  timestamp: string;
  docCount: number;
}

export interface HistogramResponse {
  histograms: Histogram[];
  intervalMinute: number;
}

export interface DeploymentLogParams {
  from: string;
  to: string;
  method: string[];
  status: string[];
  deploymentId: number;
  sort: string;
  page: number;
}

export interface Log {
  timestamp: string;
  deploymentId: string;
  ip: string;
  domain: string;
  path: string;
  method: string;
  status: string;
  duration: number;
}

export interface MethodCnt {
  DELETE: number;
  POST: number;
  GET: number;
  PATCH: number;
  PUT: number;
}

export interface StatusCnt {
  "2xx": number;
  "3xx": number;
  "4xx": number;
  "5xx": number;
}

export interface DeploymentLog {
  content: Log[];
  methodCounts: MethodCnt;
  statusCounts: StatusCnt;
}
