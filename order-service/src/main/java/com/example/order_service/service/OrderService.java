package com.example.order_service.service;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.builder.OrderBuilder;
import com.example.order_service.service.mapper.OrderResponseMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderBuilder orderBuilder;
  private final OrderResponseMapper responseMapper;

  @Autowired
  public OrderService(OrderRepository orderRepository,
      OrderBuilder orderBuilder,
      OrderResponseMapper responseMapper) {
    this.orderRepository = orderRepository;
    this.orderBuilder = orderBuilder;
    this.responseMapper = responseMapper;
  }


  public OrderResponse createOrder(OrderRequest orderRequest) {
    Order orderToSave = orderBuilder.buildOrder(orderRequest);

    Order savedOrder = orderRepository.save(orderToSave);

    return responseMapper.toDto(savedOrder);
  }

  public OrderResponse getOrder (Long userId, Long orderId) {
    Order order = orderRepository
        .findByUserIdAndId(userId, orderId)
        .orElseThrow(() -> new EntityNotFoundException(
            String.format("Заказ с id=%d для пользователя с id=%d не найден", orderId, userId)
        ));
    return responseMapper.toDto(order);
  }

  public List<OrderResponse> getOrders(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    return responseMapper.toDtoList(orders);
  }
}
