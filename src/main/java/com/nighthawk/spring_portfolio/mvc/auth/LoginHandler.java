package com.nighthawk.spring_portfolio.mvc.auth;

import com.google.gson.Gson;
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

  // Create a JWT for students
  public String createStudentJwt(String githubId) {
    var time = (new Date()).getTime() + 1000 * 60 * 60 * 24;
    return Jwts.builder()
        .setSubject(githubId)
        .setExpiration(new Date(time))
        .claim("userType", "student")  // Set user type as "student"
        .signWith(key)
        .compact();
  }

  // Create a JWT for teachers
  public String createTeacherJwt(String specialKey) {
    if ("mortCSA".equals(specialKey)) {
      var time = (new Date()).getTime() + 1000 * 60 * 60 * 24;
      return Jwts.builder()
          .setSubject("teacher")
          .setExpiration(new Date(time))
          .claim("userType", "teacher")  // Set user type as "teacher"
          .signWith(key)
          .compact();
    } else {
      throw new IllegalArgumentException("Invalid special key for teacher");
    }
  }

  // Try/catch set up for searching for an account based on a person's email
  public Person decodeJwt(String jws) {
    try {
      String githubId = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject();
      return personJpaRepository.findByEmail(githubId).get();
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  // Main method to test with an example email
  public static void main(String[] args) {
    LoginHandler handler = new LoginHandler();
    String githubId = "exampleGithubId";
    Person p = new Person();
    p.setEmail(githubId);
    String jws = handler.createStudentJwt(githubId);
    System.out.println(jws);
    System.out.println(handler.decodeJwt(jws));
  }
}
