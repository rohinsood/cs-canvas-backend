package com.nighthawk.spring_portfolio.mvc.assignments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// This annotation indicates that this class is a RESTful controller.
@RestController
// Base URL for all endpoints in this controller.
@RequestMapping("/api/assignments")
public class AssignmentController {

    // Autowiring allows Spring to automatically inject an instance of AssignmentRepository into this class.
    @Autowired
    private AssignmentRepository repository;

    // This endpoint allows for the creation of a new assignment.
    // The @PostMapping annotation indicates that this method handles POST requests.
    @PostMapping("/create")
    public ResponseEntity<Object> createAssignment(@RequestBody Assignment assignment) {
        // Check if the assignment name is empty or null.
        if (assignment.getName() == null || assignment.getName().isEmpty()) {
            return new ResponseEntity<>("Assignment name is required.", HttpStatus.BAD_REQUEST);
        }

        // Check if the GitHub raw markdown link is empty or null.
        if (assignment.getGithubRawMarkdownLink() == null || assignment.getGithubRawMarkdownLink().isEmpty()) {
            return new ResponseEntity<>("GitHub raw markdown link is required.", HttpStatus.BAD_REQUEST);
        }

        // Save the assignment to the database using the repository.
        repository.save(assignment);

        // Return a success response.
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Assignment created successfully");
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    // This method handles exceptions for all endpoints in this controller.
    // If an exception is thrown, this method will return an error response.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("error", e.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
