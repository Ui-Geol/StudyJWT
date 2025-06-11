package com.oopsw.gjwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.gjwt.dto.UserDTO;
import com.oopsw.gjwt.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  public void testJoin() {
    UserDTO userDTO = UserDTO.builder()
        .username("김탁구")
        .email("kim@gmail.com")
        .password("1234")
        .role("ROLE_USER").build();

    boolean result = userService.join(userDTO);

    assertThat(result).isTrue();

  }
}
