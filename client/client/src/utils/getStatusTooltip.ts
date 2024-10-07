import { StatusMessage } from "@/types/deploy";

export const getStatusTooptip = (
  statusMessage: StatusMessage,
  type: string
): string => {
  switch (statusMessage) {
    case StatusMessage.STOPPED:
      return `${type}이(가) 정지되었습니다.`;
    case StatusMessage.RUNNING:
      return `${type}이(가) 실행 중입니다.`;
    case StatusMessage.PENDING:
      return `${type} 생성 중입니다.`;
    case StatusMessage.DOMAIN:
      return "오류가 발생했습니다.";
    case StatusMessage.DOWNLOAD:
      return "소스코드를 다운받던 중 오류가 발생했습니다.";
    case StatusMessage.DOCKER:
      return "도커파일 생성/수정 후 다시 배포해주세요.";
    case StatusMessage.FAILED:
      return "데이터베이스 생성 중 오류가 발생했습니다.";
    case StatusMessage.CLOUD:
      return "리소스 할당 대기 중입니다.";
    default:
      return "서버가 불안정합니다.";
  }
};
