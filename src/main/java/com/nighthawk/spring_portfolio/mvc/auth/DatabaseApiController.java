package com.nighthawk.spring_portfolio.mvc.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
public class DatabaseApiController {

  @Autowired
  LoginHandler handler;

  @GetMapping("/sqlite.db")
  public ResponseEntity<Object> getDatabase(@CookieValue("flashjwt") String jwt) throws IOException {
    Person p = handler.decodeJwt(jwt);

    if (p != null) {
      // if (p.()) {
        // Teachers have access to the database
        return serveDatabaseFile();
      // } else if (p.()) {
        // Students have access to the database
        return serveDatabaseFile();
      }
    }

    // Unauthorized access
    Map<String, Object> resp = new HashMap<>();
    resp.put("err", "Unauthorized");
    return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
  }

  private ResponseEntity<Object> serveDatabaseFile() throws IOException {
    Resource databaseResource = new FileSystemResource("volumes/sqlite.db");

    if (!databaseResource.exists()) {
      throw new IOException("Database file (sqlite.db) not found");
    }

    String contentType = "application/octet-stream";

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sqlite.db\"")
        .body(databaseResource);
  }

  @ExceptionHandler({ MissingRequestCookieException.class })
  public ResponseEntity<Object> handleNoCookie() {
    Map<String, Object> resp = new HashMap<>();
    resp.put("err", "Account doesn't exist");
    return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
  }
}
