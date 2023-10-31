package com.nighthawk.spring_portfolio.mvc.auth;
// Gets necessary imports
import java.io.File;
import java.io.FileNotFoundException;
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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/db")
public class DatabaseApiController {

  @Autowired
  LoginHandler handler;

  @GetMapping("/sqlite.db")
  public ResponseEntity<Object> getDB (@CookieValue("flashjwt") String jwt) throws IOException {
    Person p = handler.decodeJwt(jwt);
    if (p.isAdmin() && p != null) {
      Resource coolDbResource = new FileSystemResource("volumes/sqlite.db");

      // Check if the file exists
      if (!coolDbResource.exists()) {
          throw new FileNotFoundException("sqlite.db file not found");
      }

      // Set the content type of the file
      String contentType = "application/octet-stream";

      // Returns the response back from the database
      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + coolDbResource.getFilename() + "\"")
              .body(coolDbResource);
    } else {
      Map<String, Object> resp = new HashMap<>();
      resp.put("err", "Unauthorized");
      return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
      
  }
}
