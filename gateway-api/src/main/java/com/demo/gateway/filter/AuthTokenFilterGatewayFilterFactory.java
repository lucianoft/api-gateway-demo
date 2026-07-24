package com.demo.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

// Filtro por rota que simula validação de token: sempre autoriza, só para estudo do mecanismo de filtro nomeado
@Slf4j
@Component
public class AuthTokenFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthTokenFilterGatewayFilterFactory.Config> {

    public AuthTokenFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (token == null || token.isBlank()) {
                log.warn("Requisição sem header Authorization — validação simulada, autorizando mesmo assim (estudo)");
            } else {
                log.info("Token recebido '{}' — validação simulada, sempre aprovada (estudo)", token);
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
