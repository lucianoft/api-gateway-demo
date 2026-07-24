package com.demo.balance.service;

import com.demo.balance.entity.Balance;
import com.demo.balance.exception.BalanceNotFoundException;
import com.demo.balance.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public Mono<List<Balance>> buscarPorConta(Long accountId) {
        return balanceRepository.findByAccountId(accountId)
                .collectList()
                .flatMap(balances -> balances.isEmpty()
                        ? Mono.error(new BalanceNotFoundException("Conta não encontrada: " + accountId))
                        : Mono.just(balances));
    }
}
