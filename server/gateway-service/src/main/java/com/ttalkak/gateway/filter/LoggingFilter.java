package com.ttalkak.gateway.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    private final Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    long endTime = System.currentTimeMillis();
                    this.logRequest(exchange, endTime - startTime);
                }));
    }

    private void logRequest(ServerWebExchange exchange, long duration) {
        URI requestUrl = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String originalUri = exchange.getRequest().getURI().toString();
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        String routeId = (route != null) ? route.getId() : "Unknown";

        Span span = tracer.currentSpan();
        String traceId = span != null ? span.context().traceId() : "Unknown";
        String spanId = span != null ? span.context().spanId() : "Unknown";

        log.info("""
                    Response: method={}, uri={}, route={}, duration={}ms, status={}, client-ip={}, x-forwarded-for={}, trace-id={}, span-id={}
                """,
                exchange.getRequest().getMethod(),
                originalUri,
                routeId,
                duration,
                exchange.getResponse().getStatusCode(),
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress(),
                exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"),
                traceId,
                spanId
        );
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
