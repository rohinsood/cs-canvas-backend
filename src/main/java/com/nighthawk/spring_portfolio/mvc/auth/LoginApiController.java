package com.nighthawk.spring_portfolio.mvc.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginApiController {
  @Autowired
  LoginHandler handler;

  @Autowired
  PersonJpaRepository personJpaRepository;

  @PostMapping("/authenticate")
  // public ResponseEntity<Object> authenticate(@RequestBody final Map<String, Object> map, @CookieValue(value = "flashjwt", required = false) String jwt, HttpServletResponse response) throws NoSuchAlgorithmException {
  
  public ResponseEntity<Object> authenticate(@RequestBody final Map<String, Object> map, HttpServletResponse response) throws NoSuchAlgorithmException {
    var popt = personJpaRepository.findByEmail((String) map.get("email"));

    if (!popt.isPresent()) {
      // error handling
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "No such user with email provided");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    var p = popt.get();

    String password = (String) map.get("password");

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedHash = digest.digest(
        password.getBytes(StandardCharsets.UTF_8));
    String computedPasswordHash = new String(encodedHash);


    if (computedPasswordHash.equals(p.getPasswordHash())) {
      // redact password
      p.passwordHash = "REDACTED";
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Incorrect Password");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    String jws = handler.createJwt(p);
    Cookie cookie = new Cookie("flashjwt", jws);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    response.addCookie(cookie);

    return new ResponseEntity<>(jws, HttpStatus.OK);
  }

  @PostMapping("/logout")
  public ResponseEntity<Object> logout(@RequestBody final Map<String, Object> map, HttpServletResponse response) {
    Cookie cookie = new Cookie("flashjwt", "");
    cookie.setPath("/");
    response.addCookie(cookie);

    Map<String, Object> resp = new HashMap<>();
    resp.put("err", false);

    return new ResponseEntity<>(resp, HttpStatus.OK);
  }

  @PostMapping("/getYourUser")
  public ResponseEntity<Object> getYourUser(@CookieValue("flashjwt") String jwt) {
    Person p = handler.decodeJwt(jwt);
    if (p == null) {
      // return err ting
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Account doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(p, HttpStatus.OK);
  }

  @ExceptionHandler({ MissingRequestCookieException.class })
  public ResponseEntity<Object> handleNoCookie() {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Account doesn't exist");
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
  }
}
