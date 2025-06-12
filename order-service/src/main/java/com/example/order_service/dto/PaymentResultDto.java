package com.example.order_service.dto;

import lombok.Data;

@Data
public class PaymentResultDto {
  private String orderId;
  private boolean success;
  private String reason;
  // геттеры/сеттеры, конструктор по-умолчанию
}