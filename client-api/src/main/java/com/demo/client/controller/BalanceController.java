package com.demo.client.controller;

import com.demo.client.dto.AccountBalanceResponse;
import com.demo.client.feign.GatewayBalanceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final GatewayBalanceClient gatewayBalanceClient;

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountBalanceResponse> buscar(@PathVariable Long accountId) {
        return ResponseEntity.ok(gatewayBalanceClient.buscar(accountId));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        return ResponseEntity.status(ex.status()).body(ex.contentUTF8());
    }
}
