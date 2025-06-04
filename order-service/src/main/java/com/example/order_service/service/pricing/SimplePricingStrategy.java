package com.example.order_service.service.pricing;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class SimplePricingStrategy implements PricingStrategy {

  @Override
  public BigDecimal calculateTotal(Order order){
    return order.getItems().stream()
        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

  }

}
