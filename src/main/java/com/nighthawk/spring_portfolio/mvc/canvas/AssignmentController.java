package com.nighthawk.spring_portfolio.mvc.canvas;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nighthawk.spring_portfolio.mvc.auth.LoginHandler;
import com.nighthawk.spring_portfolio.mvc.auth.Person;

import java.util.List;

@RestController
@RequestMapping("/api/canvas")
public class AssignmentController {

    @Autowired
    LoginHandler handler;

    @Autowired
    AssignmentRepository assRepo;

    @Autowired
    SubmissionRepository subRepo;

    @PostMapping("/createAssignment")
    public ResponseEntity<Object> createAssignment(@CookieValue("flashjwt") String jwt,
            @RequestBody final Map<String, Object> map) throws IOException {
        Person p = handler.decodeJwt(jwt);
        if (p.isAdmin() && p != null) {
            Resource coolDbResource = new FileSystemResource("volumes/sqlite.db");

            // Check if the file exists
            if (!coolDbResource.exists()) {
                throw new FileNotFoundException("sqlite.db file not found");
            }

            Assignment ass = new Assignment();
            ass.setName((String) map.get("name"));
            ass.setDescription((String) map.get("description"));
            ass.setDueDate((String) map.get("duedate"));

            assRepo.save(ass);
            Map<String, Object> response = new HashMap<>();
            response.put("err", false);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("err", "Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/getAssignmentByName")
    public ResponseEntity<Object> getAssByName(@RequestBody final Map<String, Object> map) throws IOException {
        List<Assignment> assignments = assRepo.findAssignmentByName((String) map.get("name"));
        Map<String, Object> response = new HashMap<>();

        if (assignments.isEmpty()) {
            response.put("err", "Assignment not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            response.put("assignments", assignments);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/getAllAssignments")
    public ResponseEntity<List<Assignment>> getAllAss() throws IOException {
        List<Assignment> assignments = assRepo.findAll();
        Map<String, Object> response = new HashMap<>();

        if (assignments.isEmpty()) {
            response.put("err", "Assignment not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            response.put("assignments", assignments);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        }
    }

    @PostMapping("/createSubmission")
    public ResponseEntity<Object> createSubmission(@CookieValue("flashjwt") String jwt,
            @RequestBody final Map<String, Object> map) throws IOException {
        Person p = handler.decodeJwt(jwt);

        Assignment assignment = assRepo.findById(Long.parseLong((String) map.get("assignmentID"))).orElse(null);

        if (assignment != null) {
            Submission submission = new Submission();
            submission.setLink((String) map.get("link"));
            submission.setTime((String) map.get("time"));
            submission.setEmail((String) p.getEmail());
            submission.setAssignment(assignment); // Associate submission with the assignment

            subRepo.save(submission);

            Map<String, Object> response = new HashMap<>();
            response.put("err", false);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("err", "Assignment not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllSubmissions")
    public ResponseEntity<List<Submission>> getAllSub() throws IOException {
        List<Submission> subs = subRepo.findAll();
        Map<String, Object> response = new HashMap<>();

        if (subs.isEmpty()) {
            response.put("err", "Submissions not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            response.put("submissions", subs);
            return new ResponseEntity<>(subs, HttpStatus.OK);
        }
    }

}
