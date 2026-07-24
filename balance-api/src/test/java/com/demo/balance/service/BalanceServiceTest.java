package com.demo.balance.service;

import com.demo.balance.entity.Balance;
import com.demo.balance.entity.BalanceType;
import com.demo.balance.exception.BalanceNotFoundException;
import com.demo.balance.repository.BalanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceService balanceService;

    @Test
    void deveRetornarSaldosQuandoContaExiste() {
        Balance conta = novoBalance(1L, BalanceType.CONTA, new BigDecimal("1000.00"));
        Balance limiteEspecial = novoBalance(1L, BalanceType.LIMITE_ESPECIAL, new BigDecimal("500.00"));
        when(balanceRepository.findByAccountId(1L)).thenReturn(List.of(conta, limiteEspecial));

        List<Balance> resultado = balanceService.buscarPorConta(1L);

        assertThat(resultado).containsExactly(conta, limiteEspecial);
        verify(balanceRepository).findByAccountId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoTemSaldos() {
        when(balanceRepository.findByAccountId(999L)).thenReturn(List.of());

        assertThatThrownBy(() -> balanceService.buscarPorConta(999L))
                .isInstanceOf(BalanceNotFoundException.class)
                .hasMessageContaining("999");
    }

    private Balance novoBalance(Long accountId, BalanceType type, BigDecimal amount) {
        Balance balance = new Balance();
        balance.setAccountId(accountId);
        balance.setType(type);
        balance.setAmount(amount);
        balance.setUpdatedAt(LocalDateTime.now());
        return balance;
    }
}
