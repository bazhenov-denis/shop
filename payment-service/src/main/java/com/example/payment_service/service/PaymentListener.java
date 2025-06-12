package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentRequest;
import com.example.payment_service.dto.PaymentResultPayload;
import com.example.payment_service.entity.InboxEvent;
import com.example.payment_service.repository.InboxEventRepository;
import com.example.payment_service.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class PaymentListener {

  private final ObjectMapper objectMapper;
  private final InboxEventRepository inbox;
  private final OutboxEventRepository outbox;
  private final TransactionService transactionService;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public PaymentListener(
      ObjectMapper objectMapper,
      InboxEventRepository inbox,
      OutboxEventRepository outbox,
      TransactionService transactionService
  ) {
    this.objectMapper = objectMapper;
    this.inbox = inbox;
    this.outbox = outbox;
    this.transactionService = transactionService;
  }

  @KafkaListener(
      topics = "order-payments",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
    String msgId = record.topic() + "-" + record.partition() + "-" + record.offset();
    logger.info(msgId);
    logger.info("вошли в метод принятия оплаты");
    try {
      // 1. Дедупликация
      if (inbox.existsByMessageId(msgId)) {
        return;
      }
      inbox.save(new InboxEvent(msgId, record.topic(), record.partition(), record.offset()));

      // 2. Распарсить запрос
      PaymentRequest req;
      try {
        req = objectMapper.readValue(record.value(), PaymentRequest.class);
      } catch (Exception e) {
        logger.error("Не удалось распарсить PaymentRequest, msgId={}", msgId, e);
        // можно сохранить в outbox событие failure с reason = e.getMessage()
        outbox.save(new com.example.payment_service.entity.OutboxEvent(
            msgId,
            "PaymentFailed",
            new PaymentResultPayload(null, false, "Invalid payload")
        ));
        return;
      }
      logger.info("получили данные для оплаты: номер заказа:{}, номер аккаунта:{}, сумма:{}", req.getOrderId(), req.getAccountId(), req.getAmount());
      // 3. Попытка charge
      boolean success = true;
      String reason = null;
      try {
        transactionService.charge(req.getAccountId(), req.getAmount());
      } catch (Exception e) {
        success = false;
        reason = e.getMessage();
        logger.warn("Ошибка при charge(): {}", reason);
      }

      logger.info("оплата прошла");
      // 4. Записать результат в свой outbox
      outbox.save(new com.example.payment_service.entity.OutboxEvent(
          req.getAccountId(),
          success ? "PaymentSucceeded" : "PaymentFailed",
          new PaymentResultPayload(req.getOrderId(), success, reason)
      ));

      logger.info("Записали в outbox");

    } catch (Throwable t) {
      // должен перехватить абсолютно всё, чтобы не было rollback-only
      logger.error("Непредвиденная ошибка обработки сообщения, msgId={}", msgId, t);
    } finally {
      // 5. В любых случаях – подтверждаем offset, чтобы не было retries
      ack.acknowledge();
    }
  }
}

