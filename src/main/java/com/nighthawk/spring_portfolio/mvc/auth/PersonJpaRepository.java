package com.nighthawk.spring_portfolio.mvc.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

/*
Extends the JpaRepository interface from Spring Data JPA.
-- Java Persistent API (JPA) - Hibernate: map, store, update and retrieve database
-- JpaRepository defines standard CRUD methods
-- Via JPA the developer can retrieve database from relational databases to Java objects and vice versa.
 */
// @Document
public interface PersonJpaRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
    
    Person findByEmailAndPasswordHash(String email, String passwordHash);

    @Query(
        value = "SELECT * FROM person p WHERE p.name LIKE ?1 or p.email LIKE ?1",
        nativeQuery = true)
    List<Person> findByLikeTermNative(String term);
}
