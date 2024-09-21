package com.ttalkak.deployment.common.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    UN_AUTHORIZATION(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 요청에 대한 권한이 없는 사용자입니다."),
    // 배포
    NOT_EXISTS_DEPLOYMENT(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 배포 내역이 존재하지 않습니다"),

    // 호스팅
    NOT_EXISTS_HOSTING(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "호스팅 내역이 존재하지 않습니다"),

    KAFKA_PRODUCER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.value(), "카프카 메시지 발행 도중 오류가 발생했습니다."),

    // 배포
    NOT_EXIST_VERSION(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 배포버전이 존재하지 않습니다"),

    ;
    private final HttpStatus httpStatus;

    private final Integer statusCode;

    private final String message;
}
