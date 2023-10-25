package com.nighthawk.spring_portfolio.mvc.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRoleJpaRepository extends JpaRepository<PersonRole, Long> {
    PersonRole findByName(String name);
}
