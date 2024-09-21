package com.ttalkak.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class AuthenticationHeaderFilter extends AbstractGatewayFilterFactory<AuthenticationHeaderFilter.Config> {
    public AuthenticationHeaderFilter() {
        super(Config.class);
    }

    @Value("${jwt.secret}")
    private String secret;

    private SecretKeySpec secretKey;

    @PostConstruct
    private void init() {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            String accessToken = authorizationHeader.replace("Bearer ", "");

            String userId = parseAccessToken(accessToken);

            log.debug("userId: {}", userId);

            if (userId == null) {
                return onError(exchange, "유효하지 않은 JWT 토큰 입니다.", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest newRequest = request.mutate()
                    .header("X-USER-ID", userId)
                .build();

            log.debug("newRequest: {}", newRequest);

            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        log.error("error: {}", error);
        return response.setComplete();
    }

    private String parseAccessToken(String accessToken) {
        try {
            Claims claims = parseClaims(accessToken);

            if (claims.isEmpty()) {
                log.error("토큰이 비어있습니다.");
                return null;
            }
            return claims.getSubject();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * accessToken 파싱
     *
     * @param accessToken JWT 토큰
     * @return 토큰의 클레임
     */
    private Claims parseClaims(String accessToken) {
        String message;
        Exception exception;
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            message = "유효기간이 만료된 토큰입니다.";
            exception = e;
        } catch (MalformedJwtException e) {
            message = "잘못된 형식의 토큰입니다.";
            exception = e;
        } catch (IllegalArgumentException e) {
            message = "잘못된 인자입니다.";
            exception = e;
        } catch (Exception e) {
            message = "토큰 파싱 중 에러가 발생했습니다.";
            exception = e;
        }
        throw new IllegalArgumentException(message, exception);
    }

    public static class Config {
    }
}
