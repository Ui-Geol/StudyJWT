package com.oopsw.gjwt.service;

import com.oopsw.gjwt.domain.User;
import com.oopsw.gjwt.dto.UserDTO;
import com.oopsw.gjwt.repository.UserRepository;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public boolean join(UserDTO userDTO) {
    User u1 = User.builder()
        .username(userDTO.getUsername())
        .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
        .email(userDTO.getEmail())
        .role("ROLE_USER")
        .build();
    return userRepository.save(u1) != null;
  }

}
