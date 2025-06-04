package com.example.payment_service.service;

import com.example.payment_service.dto.TransactionResponce;
import com.example.payment_service.repository.AccountTransactionRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private final AccountTransactionRepository transactionRepository;

  public TransactionService(AccountTransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public List<TransactionResponce> getTransactionsByUserId(Long userId) {
    return transactionRepository.findByAccountId(userId).
        stream()
        .map(tx -> {
          TransactionResponce dto = new TransactionResponce();
          dto.setTransactionId(tx.getId());
          dto.setType(tx.getType());
          // tx.getAmount() → строка, например "500.00"
          dto.setAmount(tx.getAmount());
          // tx.getTimestamp() → ISO-строка (например, "2025-06-04T11:22:33.456Z")
          dto.setDate(tx.getCreatedAt());
          return dto;
        })
        .collect(Collectors.toList());
  }
}
