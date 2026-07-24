package com.demo.gateway.filter;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoggingGlobalFilterTest {

    private final LoggingGlobalFilter filter = new LoggingGlobalFilter();

    @Test
    void devePassarAdianteParaOProximoFiltroDaCadeia() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/balances/1").build());
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain)).verifyComplete();

        verify(chain).filter(exchange);
    }

    @Test
    void deveConcluirSemAlterarStatusDeRespostaEmCasoDeSucesso() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/balances/1").build());
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain)).verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void devePropagarErroDaCadeia() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/balances/1").build());
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        RuntimeException falha = new RuntimeException("indisponível");
        when(chain.filter(exchange)).thenReturn(Mono.error(falha));

        StepVerifier.create(filter.filter(exchange, chain))
                .expectErrorMessage("indisponível")
                .verify();
    }

    @Test
    void deveTerOrdemDeExecucaoDefinidaComoUltima() {
        assertThat(filter.getOrder()).isEqualTo(Ordered.LOWEST_PRECEDENCE);
    }
}
