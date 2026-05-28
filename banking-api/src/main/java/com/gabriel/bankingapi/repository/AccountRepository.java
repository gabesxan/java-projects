package com.gabriel.bankingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabriel.bankingapi.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
