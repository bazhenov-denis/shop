package com.example.payment_service.entity;

import com.example.payment_service.dto.PaymentResultPayload;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
@NoArgsConstructor
public class OutboxEvent {
  @Id
  @GeneratedValue
  private Long id;
  private String aggregateId;      // здесь orderId
  private String eventType;        // PaymentSucceeded / PaymentFailed
  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private Object payload;          // любые доп. данные, напр. { orderId, amount, reason }
  private boolean processed = false;
  private Instant createdAt = Instant.now();

  public OutboxEvent(String orderId, String s, PaymentResultPayload result) {
    this.aggregateId = orderId;
    this.eventType = s;
    this.payload = result;
  }
  // + конструктор, геттеры/сеттеры
}
