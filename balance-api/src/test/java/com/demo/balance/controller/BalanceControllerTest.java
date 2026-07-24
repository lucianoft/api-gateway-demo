package com.demo.balance.controller;

import com.demo.balance.dto.AccountBalanceResponse;
import com.demo.balance.dto.BalanceItemResponse;
import com.demo.balance.entity.Balance;
import com.demo.balance.entity.BalanceType;
import com.demo.balance.exception.BalanceNotFoundException;
import com.demo.balance.mapper.BalanceMapper;
import com.demo.balance.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;

    @Mock
    private BalanceMapper balanceMapper;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        BalanceController controller = new BalanceController(balanceService, balanceMapper);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void deveRetornar200ComSaldosDaConta() {
        Balance conta = new Balance();
        when(balanceService.buscarPorConta(1L)).thenReturn(Mono.just(List.of(conta)));

        AccountBalanceResponse response = new AccountBalanceResponse(1L, List.of(
                new BalanceItemResponse(BalanceType.CONTA, new BigDecimal("1000.00")),
                new BalanceItemResponse(BalanceType.LIMITE_ESPECIAL, new BigDecimal("500.00"))
        ));
        when(balanceMapper.toResponse(eq(1L), anyList())).thenReturn(response);

        webTestClient.get().uri("/balances/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accountId").isEqualTo(1)
                .jsonPath("$.balances[0].type").isEqualTo("CONTA")
                .jsonPath("$.balances[0].amount").isEqualTo(1000.00)
                .jsonPath("$.balances[1].type").isEqualTo("LIMITE_ESPECIAL")
                .jsonPath("$.balances[1].amount").isEqualTo(500.00);
    }

    @Test
    void deveRetornar404QuandoContaNaoEncontrada() {
        when(balanceService.buscarPorConta(999L))
                .thenReturn(Mono.error(new BalanceNotFoundException("Conta não encontrada: 999")));

        webTestClient.get().uri("/balances/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}
