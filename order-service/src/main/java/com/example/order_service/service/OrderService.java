package com.example.order_service.service;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.PaymentRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OutboxEvent;
import com.example.order_service.entity.Payment;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.repository.OutboxEventRepository;
import com.example.order_service.service.builder.OrderBuilder;
import com.example.order_service.service.mapper.OrderResponseMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderBuilder orderBuilder;
  private final OrderResponseMapper responseMapper;
  private final OutboxEventRepository outbox;

  private final Logger logger = Logger.getLogger(OrderService.class.getName());

  @Autowired
  public OrderService(
      OrderRepository orderRepository,
      OrderBuilder orderBuilder,
      OrderResponseMapper responseMapper,
      OutboxEventRepository outbox
  ) {
    this.orderRepository = orderRepository;
    this.orderBuilder = orderBuilder;
    this.responseMapper = responseMapper;
    this.outbox = outbox;
  }


  public OrderResponse createOrder(OrderRequest orderRequest) {
    logger.info("Вход в метод создания заказа");
    Order orderToSave = orderBuilder.buildOrder(orderRequest);

    Order order = orderRepository.save(orderToSave);
    checkoutOrder(order);
    return responseMapper.toDto(order);
  }

  private void checkoutOrder(Order order) {
    logger.info("Вход в метод отправки заказа");
    PaymentRequest request = new PaymentRequest(order.getId(), order.getUserId(), order.getAmount());
    Payment paymentPayload = new Payment(request.getOrderId() ,request.getAccountId(), request.getAmount());


    outbox.save(new OutboxEvent(
        order.getId().toString(),
        "PaymentRequested",
        paymentPayload
    ));
  }

  public OrderResponse getOrder(Long userId, Long orderId) {
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
