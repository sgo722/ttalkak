class ErrorCodeVo {
  readonly status;
  readonly message;

  constructor(status, message) {
    this.status = status;
    this.message = message;
  }
}

export type ErrorCode = ErrorCodeVo;

export const ENTITY_NOT_FOUND = new ErrorCodeVo(
  404,
  '조회한 엔티티가 존재하지 않습니다.',
);

export const ENTITY_ALREADY_EXISTS = new ErrorCodeVo(
  409,
  '이미 존재하는 엔티티입니다.',
);

export const ALREADY_PAID = new ErrorCodeVo(409, '이미 결제가 완료되었습니다.');

export const INVALID_PAYMENT_CONTRACT = new ErrorCodeVo(
  500,
  '유효하지 않은 계정입니다.',
);

export const INVALID_INPUT = new ErrorCodeVo(
  400,
  '입력값이 유효하지 않습니다.',
);
