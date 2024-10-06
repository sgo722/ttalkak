import { DeployStatus } from "@/types/deploy";

export const getStatusTooptip = (
  status: DeployStatus,
  type: string
): string => {
  switch (status) {
    case DeployStatus.PENDING:
      return `${type} 생성 대기 중`;
    case DeployStatus.RUNNING:
      return `${type}가 실행 중입니다.`;
    case DeployStatus.STOPPED:
      return `${type}가 정지되었습니다.`;
    case DeployStatus.WAITING:
      return "리소스 할당 대기중입니다.";
    case DeployStatus.ERROR:
      return "오류가 발생했습니다.";
    default:
      return "오류가 발생했습니다.";
  }
};
