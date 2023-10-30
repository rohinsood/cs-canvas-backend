package com.nighthawk.spring_portfolio.mvc.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginHandler {
  Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  @Autowired
  PersonJpaRepository personJpaRepository;

  // creates a person's account
  public String createJwt(String customId) {
    var time = (new Date()).getTime() + 1000 * 60 * 60 * 24;
    return Jwts.builder()
      .setSubject(customId) // Set the custom ID as the subject
      .setExpiration(new Date(time))
      .signWith(key)
      .compact();
  }

  // try/catch set up for searching for an account based on a custom ID
  public Person decodeJwt(String jws) {
    try {
      String customId = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(jws)
        .getBody()
        .getSubject();

      return personJpaRepository.findByCustomId(customId).orElse(null);
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  // main method to test with an example custom ID
  public static void main(String[] args) {
    LoginHandler handler = new LoginHandler();
    String customId = "123abc"; // Replace with a custom ID
    String jws = handler.createJwt(customId);
    System.out.println(jws);
    System.out.println(handler.decodeJwt(jws));
  }
}
