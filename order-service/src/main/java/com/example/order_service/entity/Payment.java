package com.example.order_service.entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
  private Long orderId;
  private Long accountId;
  private BigDecimal amount;
}