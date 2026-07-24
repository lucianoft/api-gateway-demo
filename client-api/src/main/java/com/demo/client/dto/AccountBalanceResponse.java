package com.demo.client.dto;

import java.util.List;

public record AccountBalanceResponse(Long accountId, List<BalanceItemResponse> balances) {
}
