package com.oopsw.gjwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oopsw.gjwt.auth.PrincipalDetails;
import com.oopsw.gjwt.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    log.info("attemptAuthentication = 로그인 시도");

    // 1. 로그인 할 정보 추출
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      UserDTO u = objectMapper.readValue(request.getInputStream(), UserDTO.class);
      log.info(u.getUsername() + " " + u.getPassword());
      // 2. 로그인 시도 - form X
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
          u.getUsername(), u.getPassword());
      Authentication a = authenticationManager.authenticate(authRequest);
      PrincipalDetails details = (PrincipalDetails) a.getPrincipal();
      log.info(details.getUsername() + " " + details.getPassword());

      return a;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  //전제 조건은 로그인이 성공
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    log.info("로그인 성공");
    //3.JWT 작성
    PrincipalDetails details = (PrincipalDetails) authResult.getPrincipal();
    String jwtToken = JWT.create()
        .withSubject(details.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.TIMEOUT))
        .withClaim("id", details.getUser().getId())
        .withClaim("username", details.getUsername())
        .sign(Algorithm.HMAC512(JwtProperties.SECURET));

    //4.웹 브라우저에 전달
    response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    response.getWriter().println(Map.of("message", "loginOk"));
  }
}
