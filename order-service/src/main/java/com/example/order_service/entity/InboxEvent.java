package com.example.order_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inbox_event")
@Getter
@Setter
@NoArgsConstructor
public class InboxEvent {
  @Id
  @GeneratedValue
  private Long id;
  @Column(unique = true)
  private String messageId;      // record.topic-partition-offset
  private String topic;
  private int partition;
  @Column(name = "message_offset")
  private long offset;
  private Instant receivedAt = Instant.now();

  public InboxEvent(String msgId, String topic, int partition, long offset) {
    this.messageId = msgId;
    this.topic = topic;
    this.partition = partition;
    this.offset = offset;
  }
  // + конструктор, геттеры/сеттеры
}
