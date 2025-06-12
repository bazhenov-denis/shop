package com.example.payment_service.repository;

import com.example.payment_service.entity.OutboxEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent,Long> {
  List<OutboxEvent> findTop100ByProcessedFalseOrderByCreatedAt();
}

