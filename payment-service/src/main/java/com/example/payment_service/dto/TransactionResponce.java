package com.example.payment_service.dto;

import com.example.payment_service.enums.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TransactionResponce {
  private Long transactionId;
  private TransactionType type;
  private BigDecimal amount;
  private LocalDateTime date;

}
