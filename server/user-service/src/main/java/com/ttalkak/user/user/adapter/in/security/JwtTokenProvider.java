package com.ttalkak.user.user.adapter.in.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.PostConstruct;
import com.ttalkak.user.user.domain.JwtToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    private SecretKey secretKey;

    @Value("${jwt.access-expire}")
    private int accessExpire;

    @Value("${jwt.refresh-expire}")
    private int refreshExpire;

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     * 신규 JWT 토큰(accessToken, refreshToken) 생성
     *
     * @param userId    사용자 ID
     * @param authorities 사용자 권한
     * @return {@link JwtToken} 발급된 JWT 토큰
     */
    public JwtToken generate(Long userId, Collection<? extends GrantedAuthority> authorities) {
        String authority = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        Date accessTokenExpire = new Date(now + accessExpire);
        Date refreshTokenExpire = new Date(now + refreshExpire);

        String accessToken = Jwts.builder()
                .header().add("typ", "JWT").add("alg", "HS256")
                .and()
                .subject(userId.toString())
                .claim("authorities", authority)
                .issuedAt(new Date(now))
                .expiration(accessTokenExpire)
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(userId.toString())
                .claim("authorities", authority)
                .issuedAt(new Date(now))
                .expiration(refreshTokenExpire)
                .signWith(secretKey)
                .compact();

        return new JwtToken(accessToken, refreshToken);
    }

    /**
     * 리프레시 토큰을 이용하여 새로운 JWT 토큰을 발급한다.
     *
     * @param refreshToken 리프레시 토큰
     * @return {@link JwtToken} 발급된 JWT 토큰
     */
    public JwtToken refreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authorities").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        return generate(Long.parseLong(claims.getSubject()), authorities);
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
}
