package com.ttalkak.project.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // 프로젝트
    NOT_EXISTS_PROJECT(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 프로젝트는 존재하지 않습니다."),



    KAFKA_CHANGE_DEPLOYMENT_STATUS_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "배포 상태 변경 카프카 메시지 발행 도중 오류가 발생했습니다."),
    KAFKA_CHANGE_DOMAIN_STATUS_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "도메인 이름 변경 카프카 메시지 발행 도중 오류가 발생했습니다."),

    ALREADY_EXISTS_DOMAIN_NAME(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미 존재하는 도메인입니다."),
    ACCESS_PROJECT_DENIED(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), "해당 리소스에 대한 접근 권한이 없습니다.")
    ;



    private final HttpStatus status;
    private final Integer statusCode;
    private final String message;
}
