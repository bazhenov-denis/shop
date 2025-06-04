package com.example.order_service.service.builder;

import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.entity.Product;
import com.example.order_service.enums.OrderStatus;
import com.example.order_service.repository.ProductRepository;
import com.example.order_service.service.pricing.PricingStrategy;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class OrderBuilder {

  private final ProductRepository productRepository;
  private final OrderItemBuilder orderItemBuilder;
  private final PricingStrategy pricingStrategy;

  public OrderBuilder(ProductRepository productRepository,
      OrderItemBuilder orderItemBuilder,
      PricingStrategy pricingStrategy) {
    this.productRepository = productRepository;
    this.orderItemBuilder = orderItemBuilder;
    this.pricingStrategy = pricingStrategy;
  }

  public Order buildOrder(OrderRequest orderRequest) {
    Order order = new Order();
    String userIdStr = orderRequest.getUserId();
    Long userIdLong;
    try {
      userIdLong = Long.parseLong(userIdStr);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Неверный формат userId: " + userIdStr, e);
    }
    order.setUserId(userIdLong);
    order.setUserId(userIdLong);
    order.setStatus(OrderStatus.NEW);
    order.setCreatedAt(LocalDateTime.now());
    order.setUpdatedAt(LocalDateTime.now());

    for (OrderItemRequest itemRequest : orderRequest.getItems()) {
      Long productId = itemRequest.getProductId();
      Long quantity = itemRequest.getQuantity();

      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Product with id " + productId + " not found!"
          ));

      OrderItem orderItem = orderItemBuilder.createOrderItem(product, quantity);

      order.addItem(orderItem);
    }

    order.setAmount(pricingStrategy.calculateTotal(order));
    return order;
  }
}
