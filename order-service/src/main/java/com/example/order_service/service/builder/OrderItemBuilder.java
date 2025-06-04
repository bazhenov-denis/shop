package com.example.order_service.service.builder;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemBuilder {

  public OrderItem createOrderItem(Product product, Long quantity) {
    OrderItem item = new OrderItem();
    item.setProduct(product);
    item.setTitle(product.getTitle());
    item.setPrice(product.getPrice());
    item.setQuantity(quantity);
    return item;
  }
}
