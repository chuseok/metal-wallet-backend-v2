package com.kb.wallet.global.config;


import com.kb.wallet.global.security.TicketLoadTestAuthFilter;
import com.kb.wallet.jwt.JwtFilter;
import com.kb.wallet.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"com.kb.wallet.member", "com.kb.wallet.jwt"})
@PropertySource("classpath:application-prod.properties")
@Slf4j
public class SecurityConfig {
  @Value("${frontend.url}")
  private String frontendUrl;
  private final TokenProvider tokenProvider;
  private final TicketLoadTestAuthFilter ticketLoadTestAuthFilter;

  @Qualifier("dbUserDetailsService")
  private final UserDetailsService userDetailsService;

  @Autowired
  Environment env;

  // 비밀번호 암호화
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean(name = "prometheusUserDetailsService")
  public UserDetailsService prometheusUserDetailsService() {
    UserDetails prometheus = User.withUsername("admin")
        .password(passwordEncoder().encode("12345678"))
        .roles("PROMETHEUS")
        .build();

    return new InMemoryUserDetailsManager(prometheus);
  }

  @Bean
  @Primary
  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    // UserDetailsService와 PasswordEncoder를 설정
    authenticationManagerBuilder.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }

  @Bean
  public AuthenticationManager prometheusAuthManager(@Qualifier("prometheusUserDetailsService") UserDetailsService userDetailsService) throws Exception {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(provider);
  }

  // 클라이언트의 CORS 요청을 허용하는 설정
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin(frontendUrl);
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Bean
  @Order(1)
  public SecurityFilterChain prometheusFilterChain(HttpSecurity http) throws Exception {
    http
        .antMatcher("/api/prometheus/**")
        .authenticationManager(prometheusAuthManager(prometheusUserDetailsService()))
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    return http.build();
  }

  // 요청 경로에 대한 인증 및 인가 규칙을 정의
  @Bean
  @Order(2)
  public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            ticketLoadTestAuthFilter,
            JwtFilter.class
        )
        .authorizeHttpRequests(auth -> auth
            .antMatchers(HttpMethod.POST, "/api/tickets").authenticated()
            .antMatchers("/",
                    "/api/members/register",
                    "/api/members/login",
                    "/members/login",
                    "/api/test/**",
                    "/static/**",
                    "/resources/**",
                    "/public/**",
                    "/webjars/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/swagger-config/**",
                    "/api-docs/**",
                    "/v3/api-docs",
                    "/images/**",
                    "/favicon.ico",
                    "/error",
                    "/api/home",
                    "/api/musicals/**")
                .permitAll()

                .antMatchers("/api/musicals/*/seats-availability").authenticated()
                .antMatchers("/api/musicals/*/schedule/**").authenticated()
                .antMatchers("/api/musicals/*/booking/queue").authenticated()
                .antMatchers("/api/musicals/*/seats/reserve").authenticated()
                .antMatchers("/api/musicals/*/tickets").authenticated()
                .anyRequest().authenticated())

        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))

        .headers(headers -> headers.frameOptions(options -> options.sameOrigin()))
        .addFilterBefore(
            new JwtFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
