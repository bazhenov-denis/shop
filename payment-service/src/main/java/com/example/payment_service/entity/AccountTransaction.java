package com.example.payment_service.entity;

import com.example.payment_service.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_transactions")
@Getter
@Setter
@AllArgsConstructor
public class AccountTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Связь с таблицей accounts через внешний ключ account_id.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @Column(name = "amount", nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 20)
  private TransactionType type;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public AccountTransaction() {
  }

  /**
   * Автоматически устанавливаем createdAt при первой вставке.
   */
  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }
}
