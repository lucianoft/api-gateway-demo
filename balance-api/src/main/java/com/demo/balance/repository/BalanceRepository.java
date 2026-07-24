package com.demo.balance.repository;

import com.demo.balance.entity.Balance;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface BalanceRepository extends R2dbcRepository<Balance, Long> {

    Flux<Balance> findByAccountId(Long accountId);
}
