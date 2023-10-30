package com.nighthawk.spring_portfolio.mvc.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;

import java.util.Map;

@RestController
@CrossOrigin
public class JwtApiController {

    @Autowired
    private PersonDetailsService personDetailsService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        String name = request.get("name");

        if (id != null) {
            Person person = personDetailsService.getByEmail(id);

            if (person != null) {
                if ("mortCSA".equals(id)) {
                    person.setIsTeacher(true);
                } else {
                    person.setIsStudent(true);
                }
                person.setName(name);
                personDetailsService.save(person);
                return ResponseEntity.ok(person);
            } else {
                return ResponseEntity.badRequest().body("Invalid ID");
            }
        } else {
            return ResponseEntity.badRequest().body("Request is missing 'id' field");
        }
    }
}
