package com.kb.wallet.ticket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookResult {
  private final String email;
  private final boolean success;
  private final String message;
}
