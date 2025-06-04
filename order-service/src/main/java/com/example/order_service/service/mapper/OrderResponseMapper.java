package com.example.order_service.service.mapper;

import com.example.order_service.dto.OrderItemResponse;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderResponseMapper {

  public OrderResponse toDto(Order order) {
    List<OrderItemResponse> itemsDto = order.getItems().stream()
        .map(this::toOrderItemResponse)
        .collect(Collectors.toList());
    OrderResponse dto = new OrderResponse();
    dto.setOrderId(order.getId());
    dto.setTotalAmount(order.getAmount());
    dto.setStatus(order.getStatus().toString());
    dto.setCreatedAt(order.getCreatedAt());
    dto.setItems(itemsDto);
    return dto;
  }

  public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
    return new OrderItemResponse(
        orderItem.getProduct().getId(),
        orderItem.getTitle(),
        orderItem.getQuantity(),
        orderItem.getPrice()
    );
  }
}
