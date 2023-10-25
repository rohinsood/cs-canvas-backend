package com.nighthawk.spring_portfolio.mvc.assignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByNameContaining(String keyword);
    
    @Query(
        value = "SELECT * FROM Assignment a WHERE a.name LIKE ?1 or a.email LIKE ?1",
        nativeQuery = true)
    List<Assignment> findByLikeTermNative(String term);
}
