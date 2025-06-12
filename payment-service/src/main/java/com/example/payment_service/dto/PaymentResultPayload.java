package com.example.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Полезная нагрузка (payload) события результата оплаты,
 * которое будем сохранять в outbox_event и паблишить в Kafka.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultPayload {
  /**
   * Идентификатор заказа (тот же, что и в запросе на оплату).
   */
  private String orderId;

  /**
   * Флаг успеха: true — если оплата прошла, false — если ошибка.
   */
  private boolean success;

  /**
   * Опциональное сообщение об ошибке, если success == false.
   */
  private String reason;
}
