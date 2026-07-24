package com.demo.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// Loga toda requisição roteada pelo gateway e a resposta correspondente, com tempo total
@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long inicio = System.currentTimeMillis();

        log.info(">> {} {}", request.getMethod(), request.getURI());

        return chain.filter(exchange)
                .doOnSuccess(unused -> logResposta(exchange, request, inicio))
                .doOnError(erro -> logErro(exchange, request, inicio, erro));
    }

    private void logResposta(ServerWebExchange exchange, ServerHttpRequest request, long inicio) {
        long duracaoMs = System.currentTimeMillis() - inicio;
        log.info("<< {} {} -> status {} em {}ms",
                request.getMethod(), request.getURI(), exchange.getResponse().getStatusCode(), duracaoMs);
    }

    private void logErro(ServerWebExchange exchange, ServerHttpRequest request, long inicio, Throwable erro) {
        long duracaoMs = System.currentTimeMillis() - inicio;
        log.warn("<< {} {} -> erro '{}' em {}ms",
                request.getMethod(), request.getURI(), erro.getMessage(), duracaoMs);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
