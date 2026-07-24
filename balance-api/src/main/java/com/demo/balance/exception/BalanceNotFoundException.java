package com.demo.balance.exception;

public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException(String message) {
        super(message);
    }
}
