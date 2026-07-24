package com.demo.balance.repository;

import com.demo.balance.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    List<Balance> findByAccountId(Long accountId);
}
