// src/main/java/com/example/order_service/dto/OrderResponse.java
package com.example.order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO, которое возвращается клиенту после создания заказа.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  private Long orderId;
  private BigDecimal totalAmount;
  private String status;
  private LocalDateTime createdAt;
  private List<OrderItemResponse> items;


}
