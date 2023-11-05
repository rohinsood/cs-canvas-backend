package com.nighthawk.spring_portfolio.mvc.canvas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
// @Document
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
  List<Submission> findSubmissionByEmail(String email);
}
