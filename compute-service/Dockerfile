# 1. 빌드 환경 설정잉
FROM gradle:8.8-jdk17 AS build
WORKDIR /app

COPY . .


# 2. Gradle 빌드링
RUN gradle clean build --no-daemon

# 3. 실제 서버 실행 환경 설정잉
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 4. 빌드된 JAR 파일 복사링
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 5. 스프링 부트 앱 실행잉
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/app/app.jar"]
