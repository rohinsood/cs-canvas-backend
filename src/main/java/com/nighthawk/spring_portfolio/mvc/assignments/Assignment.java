package com.nighthawk.spring_portfolio.mvc.assignments;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Assignment {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  private String description;

  private String githubRawMarkdownLink;

  private LocalDateTime submissionTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGithubRawMarkdownLink() {
    return githubRawMarkdownLink;
  }

  public void setGithubRawMarkdownLink(String githubRawMarkdownLink) {
    this.githubRawMarkdownLink = githubRawMarkdownLink;
  }

  public LocalDateTime getSubmissionTime() {
    return submissionTime;
  }

  public void setSubmissionTime(LocalDateTime submissionTime) {
    this.submissionTime = submissionTime;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
