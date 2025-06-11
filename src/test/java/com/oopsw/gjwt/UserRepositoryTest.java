package com.oopsw.gjwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.oopsw.gjwt.domain.User;
import com.oopsw.gjwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@SpringBootTest
public class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Test
  public void addUser() {
    User u1 = User.builder()
        .username("manager")
        .password(bCryptPasswordEncoder.encode("manager"))
        .email("manager@gmail.com")
        .role("ROLE_MANAGER")
        .build();

    userRepository.save(u1);
    User savedUser = userRepository.findByUsername(u1.getUsername());

    assertThat(savedUser.getUsername()).isEqualTo(u1.getUsername());
    assertThat(bCryptPasswordEncoder.matches("manager", savedUser.getPassword())).isTrue();
    assertThat(savedUser.getEmail()).isEqualTo("manager@gmail.com");
    assertThat(savedUser.getRole()).isEqualTo("ROLE_MANAGER");


  }
}
