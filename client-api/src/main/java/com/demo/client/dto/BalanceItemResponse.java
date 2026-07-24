package com.demo.client.dto;

import java.math.BigDecimal;

public record BalanceItemResponse(BalanceType type, BigDecimal amount) {
}
