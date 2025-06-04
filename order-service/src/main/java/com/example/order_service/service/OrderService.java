package com.example.order_service.service;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.builder.OrderBuilder;
import com.example.order_service.service.mapper.OrderResponseMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderBuilder orderBuilder;
  private final OrderResponseMapper responseMapper;

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
}
