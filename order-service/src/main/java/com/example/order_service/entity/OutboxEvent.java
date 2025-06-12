package com.example.order_service.entity;

import com.example.order_service.dto.PaymentRequest;
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

  private String aggregateId;

  private String eventType;

  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private Payment payload;

  private boolean processed = false;

  private Instant createdAt = Instant.now();

  public OutboxEvent(String aggregateId, String eventType, Payment payload) {
    this.aggregateId = aggregateId;
    this.eventType   = eventType;
    this.payload     = payload;
    this.createdAt   = Instant.now();
    this.processed   = false;
  }
}
