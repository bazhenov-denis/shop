package com.example.order_service.service.pricing;

import com.example.order_service.entity.Order;
import java.math.BigDecimal;

/**
 * Стратегия расчёта итоговой суммы для Order.
 * Позволяет при необходимости создавать разные реализации (например, с учётом скидок).
 */
public interface PricingStrategy {
  /**
   * Рассчитать итоговую сумму для переданного заказа (Order).
   *
   */
  BigDecimal calculateTotal(Order order);
}