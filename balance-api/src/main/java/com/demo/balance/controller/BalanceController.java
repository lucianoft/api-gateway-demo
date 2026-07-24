package com.demo.balance.controller;

import com.demo.balance.dto.AccountBalanceResponse;
import com.demo.balance.entity.Balance;
import com.demo.balance.exception.BalanceNotFoundException;
import com.demo.balance.mapper.BalanceMapper;
import com.demo.balance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;
    private final BalanceMapper balanceMapper;

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountBalanceResponse> buscar(@PathVariable Long accountId) {
        List<Balance> balances = balanceService.buscarPorConta(accountId);
        return ResponseEntity.ok(balanceMapper.toResponse(accountId, balances));
    }

    @ExceptionHandler(BalanceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(BalanceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
