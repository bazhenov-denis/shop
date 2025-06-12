package com.example.payment_service.exceptions;

public class InsufficientFundsException extends RuntimeException {
  public InsufficientFundsException(String msg) { super(msg); }
}
