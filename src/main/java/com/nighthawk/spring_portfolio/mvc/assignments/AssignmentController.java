package com.nighthawk.spring_portfolio.mvc.assignments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentRepository repository;

    @PostMapping("/create")
    public ResponseEntity<Object> createAssignment(@RequestBody Assignment assignment) {
        // Check if the assignment name is empty
        if (assignment.getName() == null || assignment.getName().isEmpty()) {
            return new ResponseEntity<>("Assignment name is required.", HttpStatus.BAD_REQUEST);
        }

        // Check if the GitHub raw markdown link is empty
        if (assignment.getGithubRawMarkdownLink() == null || assignment.getGithubRawMarkdownLink().isEmpty()) {
            return new ResponseEntity<>("GitHub raw markdown link is required.", HttpStatus.BAD_REQUEST);
        }

        // Save the assignment to the repository
        repository.save(assignment);

        // Return a success response
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Assignment created successfully");
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error", e.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
