package com.demo.client.controller;

import com.demo.client.dto.AccountBalanceResponse;
import com.demo.client.dto.BalanceItemResponse;
import com.demo.client.dto.BalanceType;
import com.demo.client.feign.GatewayBalanceClient;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

    @Mock
    private GatewayBalanceClient gatewayBalanceClient;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BalanceController(gatewayBalanceClient)).build();
    }

    @Test
    void deveRetornar200ComSaldosDaConta() throws Exception {
        AccountBalanceResponse response = new AccountBalanceResponse(1L, List.of(
                new BalanceItemResponse(BalanceType.CONTA, new BigDecimal("1000.00")),
                new BalanceItemResponse(BalanceType.LIMITE_ESPECIAL, new BigDecimal("500.00"))
        ));
        when(gatewayBalanceClient.buscar(1L)).thenReturn(response);

        mockMvc.perform(get("/balances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.balances[0].type").value("CONTA"))
                .andExpect(jsonPath("$.balances[0].amount").value(1000.00))
                .andExpect(jsonPath("$.balances[1].type").value("LIMITE_ESPECIAL"))
                .andExpect(jsonPath("$.balances[1].amount").value(500.00));
    }

    @Test
    void deveRetornar404QuandoGatewayDevolve404() throws Exception {
        when(gatewayBalanceClient.buscar(999L)).thenThrow(notFoundDoGateway());

        mockMvc.perform(get("/balances/999"))
                .andExpect(status().isNotFound());
    }

    private FeignException notFoundDoGateway() {
        Request request = Request.create(Request.HttpMethod.GET, "/api/balances/999",
                Map.of(), null, StandardCharsets.UTF_8, new RequestTemplate());
        Response response = Response.builder()
                .status(404)
                .reason("Not Found")
                .request(request)
                .body("Conta não encontrada: 999", StandardCharsets.UTF_8)
                .build();
        return FeignException.errorStatus("GatewayBalanceClient#buscar(Long)", response);
    }
}
