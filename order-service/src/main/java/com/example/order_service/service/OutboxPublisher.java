package com.example.order_service.service;

import com.example.order_service.entity.OutboxEvent;
import com.example.order_service.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxPublisher {
  private final OutboxEventRepository outbox;
  private final KafkaTemplate<String, String> kafka;
  private final ObjectMapper objectMapper;
  private final Logger logger = LoggerFactory.getLogger(OutboxPublisher.class);
  private static final String PAYMENT_REQUEST_TOPIC = "order-payments";

  public OutboxPublisher(OutboxEventRepository outbox,
      KafkaTemplate<String, String> kafka,
      ObjectMapper objectMapper) {
    this.outbox = outbox;
    this.kafka = kafka;
    this.objectMapper = objectMapper;
  }

  @Scheduled(fixedDelay = 5000) // Проверяет каждые 5 секунд
  @Transactional
  public void publishPendingEvents() { // Убрали параметр
    logger.info("отправляем сообщение об оплате");
    List<OutboxEvent> events = outbox.findTop100ByProcessedFalseOrderByCreatedAt();

    for (OutboxEvent event : events) {
      try {
        // Сериализуем payload в JSON
        String jsonPayload = objectMapper.writeValueAsString(event.getPayload());
        // Отправляем в конкретный топик
        kafka.send(PAYMENT_REQUEST_TOPIC, event.getAggregateId(), jsonPayload);
        logger.info("отправили {}", jsonPayload);
        // Помечаем как обработанное
        event.setProcessed(true);
        outbox.save(event);
      } catch (JsonProcessingException e) {
        // Логируем ошибку
        logger.error("Ошибка сериализации: {}", e.getMessage());
      } catch (Exception e) {
        logger.error("Ошибка отправки: {}", e.getMessage());
      }
    }
  }
}