package com.example.payment_service.repository;

import com.example.payment_service.entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
  // Например, получить все транзакции по конкретному счёту:
  List<AccountTransaction> findByAccountId(Long accountId);
}
