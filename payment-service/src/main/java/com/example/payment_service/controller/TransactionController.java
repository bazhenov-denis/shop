package com.example.payment_service.controller;

import com.example.payment_service.dto.TransactionResponce;
import com.example.payment_service.service.TransactionService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class TransactionController {


  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @GetMapping("/transaction/{userId}")
  public List<TransactionResponce> getTransactions(@PathVariable Long userId) {
    return transactionService.getTransactionsByUserId(userId);
  }
}
