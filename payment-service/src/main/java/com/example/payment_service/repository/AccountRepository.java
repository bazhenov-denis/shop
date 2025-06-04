package com.example.payment_service.repository;

import com.example.payment_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {
  // При необходимости можно добавить методы вида:
  // List<Account> findByUserId(Long userId);
}
