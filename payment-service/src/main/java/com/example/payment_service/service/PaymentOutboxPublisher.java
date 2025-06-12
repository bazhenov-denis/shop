package com.example.payment_service.service;

import com.example.payment_service.entity.OutboxEvent;
import com.example.payment_service.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentOutboxPublisher {
  private final OutboxEventRepository outbox;
  private final KafkaTemplate<String,String> kafka;
  private final ObjectMapper objectMapper;
  private final Logger log = LoggerFactory.getLogger(getClass());

  public PaymentOutboxPublisher(OutboxEventRepository outbox,
      KafkaTemplate<String,String> kafka,
      ObjectMapper objectMapper) {
    this.outbox       = outbox;
    this.kafka        = kafka;
    this.objectMapper = objectMapper;
  }

  @Scheduled(fixedDelay = 5000)
  @Transactional
  public void publishPendingResults() {
    log.info("отправляем результат оплаты ={}", outbox.findAll());
    List<OutboxEvent> events = outbox.findTop100ByProcessedFalseOrderByCreatedAt();
    for (OutboxEvent e : events) {
      try {
        String payloadJson = objectMapper.writeValueAsString(e.getPayload());
        // отправляем в топик payment-results
        kafka.send("payment-results", e.getAggregateId(), payloadJson).get();

        e.setProcessed(true);
        outbox.save(e);
        log.info("Sent PaymentResult for order={}, type={}", e.getAggregateId(), e.getEventType());
      } catch (Exception ex) {
        log.error("Failed to send PaymentResult for order={}, will retry later", e.getAggregateId(), ex);
      }
    }
  }
}
