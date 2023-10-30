package com.nighthawk.spring_portfolio.mvc.assignments;

import javax.persistence.*;
import java.time.LocalDateTime;

// Entity annotation indicates that this class is a JPA entity.
// This means that a table will be created in the database based on this class.
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
  private String githubRawMarkdownLink;

  // Timestamp indicating when the assignment was submitted.
  private LocalDateTime submissionTime;

  // Getter method for the ID.
  public Long getId() {
    return id;
  }

  // Setter method for the ID.
  public void setId(Long id) {
    this.id = id;
  }

  // Getter method for the name.
  public String getName() {
    return name;
  }

  // Setter method for the name.
  public void setName(String name) {
    this.name = name;
  }

  // Getter method for the GitHub raw markdown link.
  public String getGithubRawMarkdownLink() {
    return githubRawMarkdownLink;
  }

  // Setter method for the GitHub raw markdown link.
  public void setGithubRawMarkdownLink(String githubRawMarkdownLink) {
    this.githubRawMarkdownLink = githubRawMarkdownLink;
  }

  // Getter method for the submission time.
  public LocalDateTime getSubmissionTime() {
    return submissionTime;
  }

  // Setter method for the submission time.
  public void setSubmissionTime(LocalDateTime submissionTime) {
    this.submissionTime = submissionTime;
  }

  // Getter method for the description.
  public String getDescription() {
    return description;
  }

  // Setter method for the description.
  public void setDescription(String description) {
    this.description = description;
  }
}
