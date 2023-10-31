package com.nighthawk.spring_portfolio.mvc.canvas;

import javax.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String link; // Add fields specific to a submission
    
    private String email;

    private String time;

    // Define a Many-to-One relationship with the Assignment
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

}
