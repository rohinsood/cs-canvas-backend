package com.nighthawk.spring_portfolio.mvc.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;

@RestController
@CrossOrigin
public class JwtApiController {

    @Autowired
    private PersonDetailsService personDetailsService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestParam String id, @RequestParam String name) {
        Person person = personDetailsService.getByEmail(id);
        if (person == null) {
            person = new Person();
            person.setEmail(id);
            person.setName(name);
            if ("mortCSA".equals(id)) {
                person.setIsTeacher(true);
            } else {
                person.setIsStudent(true);
            }
            personDetailsService.save(person);
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.badRequest().body("Invalid ID");
        }
    }
}
