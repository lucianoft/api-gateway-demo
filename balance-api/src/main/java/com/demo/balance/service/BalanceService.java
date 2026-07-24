package com.demo.balance.service;

import com.demo.balance.entity.Balance;
import com.demo.balance.exception.BalanceNotFoundException;
import com.demo.balance.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    @Transactional(readOnly = true)
    public List<Balance> buscarPorConta(Long accountId) {
        List<Balance> balances = balanceRepository.findByAccountId(accountId);
        if (balances.isEmpty()) {
            throw new BalanceNotFoundException("Conta não encontrada: " + accountId);
        }
        return balances;
    }
}
