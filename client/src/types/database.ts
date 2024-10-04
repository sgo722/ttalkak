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

export interface GetDatabasesResponse {
  data: GetDatabaseResponse[];
}

export interface GetDatabaseResponse {
  id: number;
  name: string;
  type: DatabaseType;
  username: string;
  password: string;
  port: number;
}
