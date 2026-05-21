package com.gabriel.bankingapi.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.gabriel.bankingapi.dto.AccountResponse;
import com.gabriel.bankingapi.dto.CreateAccountRequest;

@Service
public class AccountService {

    public AccountResponse createAccount(CreateAccountRequest request) {
        return new AccountResponse(
                1L,
                "000001",
                request.getHolderName(),
                BigDecimal.ZERO
        );
    }
}