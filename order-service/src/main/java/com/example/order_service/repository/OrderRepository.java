package com.example.order_service.repository;


import com.example.order_service.entity.Order;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByUserIdAndId(Long userId, Long orderId);

  List<Order> findByUserId(Long userId);
}
