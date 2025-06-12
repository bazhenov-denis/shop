package com.example.order_service.controller;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse createOrder(@Valid @RequestBody OrderRequest orderDto) throws JsonProcessingException {
    return orderService.createOrder(orderDto);
  }

  @GetMapping("/{userId}")
  public List<OrderResponse> getAllOrders(@PathVariable Long userId) {
    return orderService.getOrders(userId);
  }

  @GetMapping("/{userId}/{orderId}")
  public OrderResponse getOrder(@PathVariable Long userId, @PathVariable Long orderId) {
    return orderService.getOrder(userId, orderId);
  }
}
