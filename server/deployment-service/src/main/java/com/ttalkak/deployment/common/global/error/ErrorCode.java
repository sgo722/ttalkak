package com.ttalkak.deployment.common.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    UN_AUTHORIZATION(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), "해당 요청에 대한 권한이 없는 사용자입니다."),
    // 배포
    NOT_EXISTS_DEPLOYMENT(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 배포 내역이 존재하지 않습니다"),

    NOT_DETECTED_GIT_REPOSITORY(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.value(), "깃허브 서버 상태가 불안정합니다."),

    // 호스팅
    NOT_EXISTS_HOSTING(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "호스팅 내역이 존재하지 않습니다"),

    NOT_EXISTS_DATABASE(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "데이터베이스 정보가 존재하지 않습니다"),


    KAFKA_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "카프카 메시지 발행 도중 오류가 발생했습니다."),

    KAFKA_CHANGE_DATABASE_STATUS_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 상태 변경 카프카 메시지 발행 도중 오류가 발생했습니다."),

    KAFKA_CREATE_DATABASE_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 생성 카프카 메시지 발행 도중 오류가 발생했습니다."),

    KAFKA_CHANGE_DEPLOYMENT_STATUS_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "배포 상태 변경 카프카 메시지 발행 도중 오류가 발생했습니다."),

    KAFKA_CHANGE_PAVICON_STATUS_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "파비콘 변경 카프카 메시지 발행 도중 오류가 발생했습니다."),

    KAFKA_CREATE_INSTANCE_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "배포 생성 카프카 메시지 발행 도중 오류가 발생했습니다."),

    KAFKA_REBUILD_INSTANCE_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "배포 재생성 카프카 메시지 발행 도중 오류가 발생했습니다."),

    KAFKA_FAVICON_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "파비콘 관련 카프카 메시지 발행 도중 오류가 발생했습니다."),

    KAFKA_DOMAIN_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "도메인 관련 카프카 메시지 발행 도중 오류가 발생했습니다."),

    // 배포
    NOT_EXIST_VERSION(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 배포버전이 존재하지 않습니다"),

    GITHUB_FEIGN_ERROR(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.value(), "git feign 요청 중 오류가 발생했습니다."),

    GPT_NOT_CREATE_DOCKERFILE(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.value(), "Dockerfile 생성 실패 요청 중 오류가 발생했습니다."),
    ;
    private final HttpStatus httpStatus;

    private final Integer statusCode;

    private final String message;
}
