package com.example.payment_service.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "balance", nullable = false, precision = 19, scale = 2)
  private BigDecimal balance = BigDecimal.ZERO;

  /**
   * Список всех транзакций, связанных с этим счётом.
   * Ленивая загрузка, чтобы при выборке счёта
   * не сразу тащить все транзакции из БД.
   */
  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<AccountTransaction> transactions = new ArrayList<>();

  public Account() {
  }

  public Account(Long userId, BigDecimal initialBalance) {
    this.userId = userId;
    this.balance = initialBalance;
  }

  /**
   * Удобный метод: добавляем транзакцию к этому счёту и
   * автоматически выставляем связь обратную (account).
   */
  public void addTransaction(AccountTransaction tx) {
    tx.setAccount(this);
    this.transactions.add(tx);
  }

  /**
   * Удаляет транзакцию из списка и разрывает связь.
   */
  public void removeTransaction(AccountTransaction tx) {
    this.transactions.remove(tx);
    tx.setAccount(null);
  }
}
