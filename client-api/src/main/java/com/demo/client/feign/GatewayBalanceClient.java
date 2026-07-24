package com.demo.client.feign;

import com.demo.client.dto.AccountBalanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway-api", url = "${app.gateway-api.base-url}", path = "/api")
public interface GatewayBalanceClient {

    @GetMapping("/balances/{accountId}")
    AccountBalanceResponse buscar(@PathVariable("accountId") Long accountId);
}
