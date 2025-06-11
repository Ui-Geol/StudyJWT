package com.oopsw.gjwt.jwt;


public interface JwtProperties {

  String SECURET = "oopsw";
  int TIMEOUT = 60 * 60 * 1000; //millisecond
  String TOKEN_PREFIX = "Bearer ";
  String HEADER_STRING = "Authorization";

}
