package com.gabriel.bankingapi.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String holderName;
    private BigDecimal balance;

    public AccountResponse(Long id, String accountNumber, String holderName, BigDecimal balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}