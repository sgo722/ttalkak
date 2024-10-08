import { DeployStatus } from "@/types/deploy";
import { StatusMessage } from "@/types/deploy";

export enum DatabaseType {
  MYSQL = "MYSQL",
  MARIADB = "MARIADB",
  MONGODB = "MONGODB",
  POSTGRESQL = "POSTGRESQL",
  REDIS = "REDIS",
}

export interface CreateDatabaseRequest {
  type: DatabaseType;
  name: string;
}

export interface GetDatabasesParams {
  page: number;
  size: number;
  sort: string;
  direction: string;
  searchKeyword?: string;
}

export interface GetDatabasesResponse {
  content: GetDatabasesContentResponse[];
  totalPages: number;
  totalElements: number;
}

export interface GetDatabasesContentResponse {
  id: number;
  name: string;
  type: DatabaseType;
  port: number;
  status: DeployStatus;
  statusMessage: StatusMessage;
}

export interface GetDatabaseResponse {
  dbName: string;
  id: number;
  name: string;
  password: string;
  port: number;
  status: DeployStatus;
  statusMessage: StatusMessage;
  type: DatabaseType;
  username: string;
  host?: string;
}
