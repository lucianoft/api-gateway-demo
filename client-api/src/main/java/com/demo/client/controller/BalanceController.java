package com.demo.client.controller;

import com.demo.client.dto.AccountBalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpRequest;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final RestClient gatewayRestClient;

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountBalanceResponse> buscar(@PathVariable Long accountId) {
        return gatewayRestClient.get()
                .uri("/api/balances/{accountId}", accountId)
                .exchange(this::forward);
    }

    private ResponseEntity<AccountBalanceResponse> forward(
            HttpRequest request,
            RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response) throws IOException {
        HttpStatusCode status = response.getStatusCode();
        AccountBalanceResponse body = status.is2xxSuccessful() ? response.bodyTo(AccountBalanceResponse.class) : null;
        return ResponseEntity.status(status).body(body);
    }
}
