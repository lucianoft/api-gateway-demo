package com.demo.balance.dto;

import com.demo.balance.entity.BalanceType;

import java.math.BigDecimal;

public record BalanceItemResponse(BalanceType type, BigDecimal amount) {
}
