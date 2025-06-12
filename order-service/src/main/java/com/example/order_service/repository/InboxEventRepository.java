package com.example.order_service.repository;

import com.example.order_service.entity.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxEventRepository extends JpaRepository<InboxEvent,Long> {
  boolean existsByMessageId(String messageId);
}