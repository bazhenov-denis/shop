package com.example.order_service.service;

import com.example.order_service.dto.PaymentResultDto;
import com.example.order_service.entity.InboxEvent;
import com.example.order_service.entity.Order;
import com.example.order_service.enums.OrderStatus;
import com.example.order_service.repository.InboxEventRepository;
import com.example.order_service.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentResultListener {

  private final InboxEventRepository inbox;
  private final OrderRepository orders;
  private final Logger log = LoggerFactory.getLogger(getClass());
  private final ObjectMapper objectMapper;


  public PaymentResultListener(InboxEventRepository inbox, OrderRepository orders, ObjectMapper objectMapper) {
    this.inbox = inbox;
    this.orders = orders;
    this.objectMapper = objectMapper;
  }

  @KafkaListener(topics = "payment-results", containerFactory = "kafkaListenerContainerFactory")
  @Transactional
  public void onPaymentResult(ConsumerRecord<String,String> record, Acknowledgment ack) throws Exception {
    log.info("получили результат оплаты: {}", record.value());
    String msgId = record.topic()+"-"+record.partition()+"-"+record.offset();

    if (inbox.existsByMessageId(msgId)) {
      ack.acknowledge();
      return;  // уже обрабатывали
    }

    inbox.save(new InboxEvent(msgId, record.topic(), record.partition(), record.offset()));

    PaymentResultDto result = objectMapper.readValue(record.value(), PaymentResultDto.class);
    Long orderId = Long.valueOf(result.getOrderId());
    Order order = orders.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException(orderId+" not found"));

    order.setStatus(result.isSuccess() ? OrderStatus.PAYMENT : OrderStatus.CANCELED);
    orders.save(order);

    ack.acknowledge();
    log.info("Order {} updated to {}", orderId, order.getStatus());
  }
}
