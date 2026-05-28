package com.gabriel.bankingapi.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.gabriel.bankingapi.dto.AccountResponse;
import com.gabriel.bankingapi.dto.CreateAccountRequest;
import com.gabriel.bankingapi.entity.Account;
import com.gabriel.bankingapi.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        String accountNumber = "ACC-" + System.currentTimeMillis();

        Account account = new Account(
                accountNumber,
                request.getHolderName(),
                BigDecimal.ZERO);

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getHolderName(),
                savedAccount.getBalance());
    }
}
