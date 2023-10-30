// Package declaration specifying the directory structure this file resides in within the larger project.
package com.nighthawk.spring_portfolio.mvc.auth;

// Importing the JpaRepository class from the 'org.springframework.data.jpa.repository' package.
// JpaRepository is a JPA specific extension of Repository for standard CRUD operations.
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Public interface declaration for a JPA repository that will work with 'PersonRole' entities.
 * This interface extends JpaRepository, inheriting methods for high-level CRUD functionality.
 * The generic parameters '<PersonRole, Long>' indicate that this is a repository for 'PersonRole' entities
 * with a primary key of type 'Long'.
 */
public interface PersonRoleJpaRepository extends JpaRepository<PersonRole, Long> {

    // Method declaration to find a 'PersonRole' entity by its 'name' attribute.
    // This method will be used to fetch a role from the database corresponding to the given name.
    PersonRole findByName(String name);
}
