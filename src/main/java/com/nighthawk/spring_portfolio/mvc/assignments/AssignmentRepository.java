package com.nighthawk.spring_portfolio.mvc.assignments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

// This interface defines the repository for the Assignment entity.
// By extending JpaRepository, it inherits several methods for working with the database.
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // This method retrieves all assignments whose names contain the specified keyword.
    List<Assignment> findByNameContaining(String keyword);
    
    // This custom query retrieves assignments based on a search term.
    // It searches both the name and email fields for matches.
    // The @Query annotation allows for custom SQL queries.
    @Query(
        value = "SELECT * FROM Assignment a WHERE a.name LIKE ?1 or a.email LIKE ?1",
        nativeQuery = true)
    List<Assignment> findByLikeTermNative(String term);
}
