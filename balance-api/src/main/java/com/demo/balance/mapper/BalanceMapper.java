package com.demo.balance.mapper;

import com.demo.balance.dto.AccountBalanceResponse;
import com.demo.balance.dto.BalanceItemResponse;
import com.demo.balance.entity.Balance;
import com.demo.balance.entity.BalanceType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BalanceMapper {

    public AccountBalanceResponse toResponse(Long accountId, List<Balance> balances) {
        List<BalanceItemResponse> itens = balances.stream()
                .map(balance -> new BalanceItemResponse(BalanceType.valueOf(balance.getType()), balance.getAmount()))
                .toList();
        return new AccountBalanceResponse(accountId, itens);
    }
}
