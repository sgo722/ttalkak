# 1. 빌드 환경 설정
FROM gradle:8.8-jdk17 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradle.properties ./

COPY gradle ./gradle

# 2. Gradle 빌드
RUN gradle clean build --no-daemon

# 3. 실제 서버 실행 환경 설정
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 4. 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 5. 스프링 부트 앱 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/app/app.jar"]
