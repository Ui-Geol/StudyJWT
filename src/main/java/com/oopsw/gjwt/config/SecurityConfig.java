package com.oopsw.gjwt.config;

import com.oopsw.gjwt.jwt.JwtAuthorizationFilter;
import com.oopsw.gjwt.jwt.JwtBasicAuthenticationFilter;
import com.oopsw.gjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private CorsFilter corsFilter;

  @Bean
  public AuthenticationManager authenticationManagerBean(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {

    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      AuthenticationManager authenticationManager, UserRepository userRepository) throws Exception {
    http.csrf(csrf -> csrf.disable());
    //화살표 함수를 쓰는 이유: 재사용 할 일이 없는 클래스를 생성할 때
    //jsession id가 발생되지 않는다
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.formLogin(form -> form.disable());
    http.httpBasic(httpBasic -> httpBasic.disable());
    http.addFilter(corsFilter);
    http.addFilter(new JwtAuthorizationFilter(authenticationManager));
    http.addFilter(new JwtBasicAuthenticationFilter(authenticationManager, userRepository));
    http.authorizeHttpRequests(auth ->
        auth.requestMatchers("api/v1/user/**").authenticated() //컨트롤러 필요
            .requestMatchers("api/v1/admin/**").hasAnyRole("ADMIN", "MANAGER") // 컨트롤러 필요
            .requestMatchers("api/v1/manager/**").hasAnyRole("MANAGER") // 컨트롤러 필요
            .anyRequest().permitAll());
    return http.build();
  }

}
