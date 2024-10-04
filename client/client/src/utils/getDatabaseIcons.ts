import { DatabaseType } from "@/types/database";
import {
  SiMongodb,
  SiPostgresql,
  SiRedis,
  SiMariadbfoundation,
} from "react-icons/si";
import { GrMysql } from "react-icons/gr";
import { IconType } from "react-icons";

export const databaseOptions = [
  { name: "MySQL", type: DatabaseType.MYSQL, icon: GrMysql },
  { name: "PostgreSQL", type: DatabaseType.POSTGRESQL, icon: SiPostgresql },
  { name: "MongoDB", type: DatabaseType.MONGODB, icon: SiMongodb },
  { name: "Redis", type: DatabaseType.REDIS, icon: SiRedis },
  { name: "MariaDB", type: DatabaseType.MARIADB, icon: SiMariadbfoundation },
];

export const getDatabaseIcon = (type: DatabaseType): IconType => {
  const option = databaseOptions.find((opt) => opt.type === type);
  return option ? option.icon : GrMysql;
};

export const getDatabaseName = (type: DatabaseType): string => {
  const option = databaseOptions.find((opt) => opt.type === type);
  return option ? option.name : "Unknown";
};
