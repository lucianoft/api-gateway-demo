package com.demo.balance.mapper;

import com.demo.balance.dto.AccountBalanceResponse;
import com.demo.balance.entity.Balance;
import com.demo.balance.entity.BalanceType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BalanceMapperTest {

    private final BalanceMapper mapper = new BalanceMapper();

    @Test
    void deveMapearListaDeSaldosParaResponse() {
        Balance conta = novoBalance(1L, BalanceType.CONTA, new BigDecimal("1000.00"));
        Balance limiteEspecial = novoBalance(1L, BalanceType.LIMITE_ESPECIAL, new BigDecimal("500.00"));

        AccountBalanceResponse response = mapper.toResponse(1L, List.of(conta, limiteEspecial));

        assertThat(response.accountId()).isEqualTo(1L);
        assertThat(response.balances()).hasSize(2);
        assertThat(response.balances().get(0).type()).isEqualTo(BalanceType.CONTA);
        assertThat(response.balances().get(0).amount()).isEqualByComparingTo("1000.00");
        assertThat(response.balances().get(1).type()).isEqualTo(BalanceType.LIMITE_ESPECIAL);
        assertThat(response.balances().get(1).amount()).isEqualByComparingTo("500.00");
    }

    @Test
    void deveMapearListaVaziaParaResponseSemSaldos() {
        AccountBalanceResponse response = mapper.toResponse(1L, List.of());

        assertThat(response.accountId()).isEqualTo(1L);
        assertThat(response.balances()).isEmpty();
    }

    private Balance novoBalance(Long accountId, BalanceType type, BigDecimal amount) {
        Balance balance = new Balance();
        balance.setAccountId(accountId);
        balance.setType(type.name());
        balance.setAmount(amount);
        balance.setUpdatedAt(LocalDateTime.now());
        return balance;
    }
}
