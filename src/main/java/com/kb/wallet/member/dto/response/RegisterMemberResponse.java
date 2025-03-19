package com.kb.wallet.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMemberResponse {

  private long id;
  private String email;
  private String name;
}
