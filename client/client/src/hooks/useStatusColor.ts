import { useMemo } from "react";
import { DeployStatus } from "@/types/deploy";

const useStatusColor = (status?: DeployStatus) => {
  const statusColor = useMemo(() => {
    if (!status) return "bg-gray-500";

    switch (status) {
      case DeployStatus.STOPPED:
      case DeployStatus.ERROR:
        return "bg-red-500";
      case DeployStatus.RUNNING:
        return "bg-green-500";
      case DeployStatus.PENDING:
      case DeployStatus.WAITING:
        return "bg-yellow-400 animate-pulse";
      default:
        return "bg-gray-500";
    }
  }, [status]);

  return statusColor;
};

export default useStatusColor;
