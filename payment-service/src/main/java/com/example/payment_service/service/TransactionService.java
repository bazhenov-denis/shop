// src/main/java/com/example/payment_service/service/TransactionService.java
package com.example.payment_service.service;

import com.example.payment_service.dto.TransactionResponce;
import com.example.payment_service.entity.Account;
import com.example.payment_service.entity.AccountTransaction;
import com.example.payment_service.enums.TransactionType;
import com.example.payment_service.exceptions.InsufficientFundsException;
import com.example.payment_service.repository.AccountRepository;
import com.example.payment_service.repository.AccountTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

  private final AccountRepository accountRepository;
  private final AccountTransactionRepository transactionRepository;
  private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

  public TransactionService(AccountRepository accountRepository,
      AccountTransactionRepository transactionRepository) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  public List<TransactionResponce> getTransactionsByUserId(Long userId) {
    return transactionRepository.findByAccountId(userId).stream()
        .map(tx -> {
          TransactionResponce dto = new TransactionResponce();
          dto.setTransactionId(tx.getId());
          dto.setType(tx.getType());
          dto.setAmount(tx.getAmount());
          dto.setDate(tx.getCreatedAt());
          return dto;
        })
        .collect(Collectors.toList());
  }

  /**
   * Снимает указанную сумму со счёта и сохраняет транзакцию.
   */
  @Transactional
  public void charge(String accountIdStr, BigDecimal amount) {
    logger.info("Вход в метод оплаты с account " + accountIdStr, "и суммой " + amount);
    Long accountId = Long.valueOf(accountIdStr);
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() ->
            new IllegalArgumentException("Account not found: " + accountId));

    if (account.getBalance().compareTo(amount) < 0) {
      throw new InsufficientFundsException(
          "Not enough balance on account " + accountId);
    }

    // обновляем баланс
    account.setBalance(account.getBalance().subtract(amount));
    accountRepository.save(account);
    logger.info("текущий баланс {}", account.getBalance());
    // сохраняем запись транзакции
    AccountTransaction tx = new AccountTransaction();
    tx.setAccount(account);
    tx.setAmount(amount);
    tx.setType(TransactionType.PAYMENT);
    tx.setCreatedAt(LocalDateTime.now());
    transactionRepository.save(tx);
  }
}
