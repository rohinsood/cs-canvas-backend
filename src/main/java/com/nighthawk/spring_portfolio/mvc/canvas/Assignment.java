package com.nighthawk.spring_portfolio.mvc.canvas;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// Entity annotation indicates that this class is a JPA entity.
// This means that a table will be created in the database based on this class.
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Assignment {

  // The @Id annotation specifies the primary key of the table.
  // @GeneratedValue specifies how the primary key should be generated.
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  // Name of the assignment.
  private String name;

  // Description of the assignment.
  private String description;

  // Link to the raw markdown content on GitHub for the assignment.
  private String dueDate;

  @OneToMany(mappedBy = "assignment")
  private List<Submission> submissions;

}
