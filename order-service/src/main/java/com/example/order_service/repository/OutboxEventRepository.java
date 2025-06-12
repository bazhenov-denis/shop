package com.example.order_service.repository;

import com.example.order_service.entity.OutboxEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
  List<OutboxEvent> findTop100ByProcessedFalseOrderByCreatedAt();
}