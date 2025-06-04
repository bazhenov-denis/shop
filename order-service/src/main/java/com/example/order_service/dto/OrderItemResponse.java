package com.example.order_service.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Возвращаемая информация по каждой позиции заказанного товара.
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
  private Long productId;
  private String title;
  private Long quantity;
  private BigDecimal price;

}
