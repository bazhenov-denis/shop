package com.example.payment_service.service;

import com.example.payment_service.dto.AccountResponce;
import com.example.payment_service.entity.Account;
import com.example.payment_service.repository.AccountRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public AccountResponce createAccount(Long userId) {
    Account account = new Account();
    account.setUserId(userId);
    account.setBalance(BigDecimal.ZERO);
    accountRepository.save(account);
    AccountResponce accountResponce = new AccountResponce();
    accountResponce.setAccountId(account.getId());
    accountResponce.setBalance(account.getBalance().longValue());
    return accountResponce;
  }

  public AccountResponce findAccountById(Long accountId) {
    Account account = accountRepository.findById(accountId).orElse(null);
    AccountResponce accountResponce = new AccountResponce();
    accountResponce.setAccountId(account.getId());
    accountResponce.setBalance(account.getBalance().longValue());
    return accountResponce;
  }

  public AccountResponce topUpBalance(Long userId, BigDecimal amount) {
    Account account = accountRepository.findById(userId).get();
    account.setBalance(account.getBalance().add(amount));
    accountRepository.save(account);
    AccountResponce accountResponce = new AccountResponce();
    accountResponce.setAccountId(account.getId());
    accountResponce.setBalance(account.getBalance().longValue());
    return accountResponce;
  }
}
