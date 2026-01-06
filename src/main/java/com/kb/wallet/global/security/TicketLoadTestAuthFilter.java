package com.kb.wallet.global.security;

import com.kb.wallet.member.domain.Member;
import com.kb.wallet.member.repository.MemberRepository;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TicketLoadTestAuthFilter extends OncePerRequestFilter {

  private final MemberRepository memberRepository;

  public TicketLoadTestAuthFilter(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    if ("POST".equals(request.getMethod())
        && "/api/tickets".equals(request.getRequestURI())
        && SecurityContextHolder.getContext().getAuthentication() == null) {

      String email = request.getHeader("X-LOADTEST-EMAIL");

      if (email == null || email.isBlank()) {
        filterChain.doFilter(request, response);
        return;
      }

      Member member = memberRepository.findByEmail(email)
          .orElseThrow(() -> new RuntimeException("Load test member not found: " + email));

      Authentication authentication =
          new UsernamePasswordAuthenticationToken(
              member,
              null,
              member.getAuthorities()
          );


      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}