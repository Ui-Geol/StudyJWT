package com.oopsw.gjwt.controller;

import com.oopsw.gjwt.dto.UserDTO;
import com.oopsw.gjwt.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

  private final UserService userService;

  @GetMapping("/api/v1/user")
  public Map<String, String> getUser(Authentication auth) {
    return Map.of("message", "user=" + auth);
  }

  @GetMapping("/api/v1/amdin")
  public Map<String, String> getAdmin(Authentication auth) {
    return Map.of("message", "admin=" + auth);
  }

  @GetMapping("/api/v1/manager")
  public Map<String, String> getManager(Authentication auth) {
    return Map.of("message", "manager=" + auth);
  }

  @PostMapping("join")
  public ResponseEntity<Map<String, String>> join(@RequestBody UserDTO userDTO) {
    if (userService.join(userDTO)) {
      return ResponseEntity.ok().body(Map.of("message", "ok"));
    }

    return ResponseEntity.status(401).body(Map.of("message", "error"));
  }

  @GetMapping("/main")
  public Map<String, String> main() {
    return Map.of("message", "Hello World");
  }

}
