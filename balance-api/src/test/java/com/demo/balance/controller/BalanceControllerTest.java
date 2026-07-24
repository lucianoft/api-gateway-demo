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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;

    @Mock
    private BalanceMapper balanceMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        BalanceController controller = new BalanceController(balanceService, balanceMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deveRetornar200ComSaldosDaConta() throws Exception {
        Balance conta = new Balance();
        when(balanceService.buscarPorConta(1L)).thenReturn(List.of(conta));

        AccountBalanceResponse response = new AccountBalanceResponse(1L, List.of(
                new BalanceItemResponse(BalanceType.CONTA, new BigDecimal("1000.00")),
                new BalanceItemResponse(BalanceType.LIMITE_ESPECIAL, new BigDecimal("500.00"))
        ));
        when(balanceMapper.toResponse(eq(1L), anyList())).thenReturn(response);

        mockMvc.perform(get("/balances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.balances[0].type").value("CONTA"))
                .andExpect(jsonPath("$.balances[0].amount").value(1000.00))
                .andExpect(jsonPath("$.balances[1].type").value("LIMITE_ESPECIAL"))
                .andExpect(jsonPath("$.balances[1].amount").value(500.00));
    }

    @Test
    void deveRetornar404QuandoContaNaoEncontrada() throws Exception {
        when(balanceService.buscarPorConta(999L))
                .thenThrow(new BalanceNotFoundException("Conta não encontrada: 999"));

        mockMvc.perform(get("/balances/999"))
                .andExpect(status().isNotFound());
    }
}
