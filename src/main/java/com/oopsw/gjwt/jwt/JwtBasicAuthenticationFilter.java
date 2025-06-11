package com.oopsw.gjwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oopsw.gjwt.auth.PrincipalDetails;
import com.oopsw.gjwt.domain.User;
import com.oopsw.gjwt.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtBasicAuthenticationFilter extends BasicAuthenticationFilter {

  private UserRepository userRepository;

  public JwtBasicAuthenticationFilter(AuthenticationManager authenticationManager,
      UserRepository userRepository) {
    super(authenticationManager);
    this.userRepository = userRepository;
    log.info("JwtBasicAuthenticationFilter");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    log.info("doFilterInternal =>" + request.getRequestURI());

    String jwtToken = request.getHeader(JwtProperties.HEADER_STRING);
    log.info("jwtToken =>" + jwtToken);
    if (jwtToken == null || jwtToken.trim().length() == 0 || !jwtToken.startsWith(
        JwtProperties.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    //2. JWT 토큰
    String token = jwtToken.replace(JwtProperties.TOKEN_PREFIX, "");

    //3. username
    String username = JWT.require(Algorithm.HMAC256(JwtProperties.SECURET)).build().verify(token)
        .getClaim("username").asString();

    //4. 유용한 계정인지 확인
    if (username != null) {
      //DBMS 계정 여부 확인
      User u = userRepository.findByUsername(username);
      PrincipalDetails details = new PrincipalDetails(u);
      //세션 접근
      Authentication auth = new UsernamePasswordAuthenticationToken(details, null,
          details.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    chain.doFilter(request, response);
  }
}
