package com.example.payment_service.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentRequest {
  private String orderId;
  private String accountId;
  private BigDecimal amount;

  public PaymentRequest() {}

}
