package com.example.payment_service.controller;

import com.example.payment_service.dto.AccountResponce;
import com.example.payment_service.service.AccountService;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  // Создать аккаунт для пользователя
  @PostMapping("create/{userId}")
  @ResponseStatus(HttpStatus.CREATED)
  public AccountResponce createAccount(@PathVariable Long userId) {
    return accountService.createAccount(userId);
  }

  // Получить информацию об аккаунте пользователя
  @GetMapping("/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public AccountResponce getAccount(@PathVariable Long userId) {
    return accountService.findAccountById(userId);
  }


  // Пополнить баланс
  @PutMapping("topup/{userId}/{amount}")
  @ResponseStatus(HttpStatus.OK)
  public AccountResponce topUpBalance(
      @PathVariable Long userId,
      @PathVariable BigDecimal amount
  ) {
    return accountService.topUpBalance(userId, amount);
  }

}
