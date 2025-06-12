package com.example.payment_service.service;

import com.example.payment_service.dto.AccountResponce;
import com.example.payment_service.entity.Account;
import com.example.payment_service.entity.AccountTransaction;
import com.example.payment_service.enums.TransactionType;
import com.example.payment_service.repository.AccountRepository;
import com.example.payment_service.repository.AccountTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountTransactionRepository accountTransactionRepository;

  public AccountService(AccountRepository accountRepository, AccountTransactionRepository accountTransactionRepository) {
    this.accountRepository = accountRepository;
    this.accountTransactionRepository = accountTransactionRepository;
  }

  public AccountResponce createAccount(Long userId) {
    Account account = new Account();
    account.setUserId(userId);
    account.setBalance(BigDecimal.ZERO);
    accountRepository.save(account);
    AccountResponce accountResponce = new AccountResponce();
    accountResponce.setUserId(account.getId());
    accountResponce.setBalance(account.getBalance().longValue());
    return accountResponce;
  }

  public AccountResponce findAccountById(Long accountId) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() ->
            new EntityNotFoundException("Account not found with id=" + accountId)
        );    AccountResponce accountResponce = new AccountResponce();
    accountResponce.setUserId(account.getId());
    accountResponce.setBalance(account.getBalance().longValue());
    return accountResponce;
  }

  public AccountResponce topUpBalance(Long userId, BigDecimal amount) {
    Account account = accountRepository.findById(userId).get();
    account.setBalance(account.getBalance().add(amount));
    accountRepository.save(account);

    AccountTransaction accountTransaction = new AccountTransaction();
    accountTransaction.setAmount(amount);
    accountTransaction.setType(TransactionType.TOPUP);
    accountTransaction.setAccount(account);
    accountTransaction.prePersist();

    accountTransactionRepository.save(accountTransaction);
    AccountResponce accountResponce = new AccountResponce();
    accountResponce.setUserId(account.getUserId());
    accountResponce.setBalance(account.getBalance().longValue());
    return accountResponce;
  }
}
