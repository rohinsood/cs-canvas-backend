package com.nighthawk.spring_portfolio.mvc.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.*;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonDetailsService implements UserDetailsService {
    
    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private PersonRoleJpaRepository personRoleJpaRepository;

    // Probably don't need this
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        // Retrieve a person by their email from the database
        Optional<Person> person = personJpaRepository.findByEmail(email);

        // If the person doesn't exist, throw an exception
        if (!person.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Create a collection of authorities (roles) for the user
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        person.get().getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        // Create and return a UserDetails object, which Spring Security will use for authentication
        return new User(person.get().getEmail(), person.get().getPasswordHash(), authorities);
    }

    // Person-related methods

    // List all persons in the database
    public List<Person> listAll() {
        return personJpaRepository.findAll();
    }

    // List persons by their email
    public List<Person> list(String email) {
        return Arrays.asList(personJpaRepository.findByEmail(email).get());
    }

    // List persons with an email similar to the provided search term using a native query
    public List<Person> listLikeNative(String term) {
        String like_term = String.format("%%%s%%", term);
        return personJpaRepository.findByLikeTermNative(like_term);
    }

    // Save a Person object to the database
    public void save(Person person) {
        // Ensure the password hash is set properly before saving
        person.setPasswordHash(person.getPasswordHash());
        personJpaRepository.save(person);
    }

    // Get a Person by their ID
    public Person get(long id) {
        return (personJpaRepository.findById(id).isPresent()) ? personJpaRepository.findById(id).get() : null;
    }

    // Get a Person by their email
    public Person getByEmail(String email) {
        return personJpaRepository.findByEmail(email).get();
    }

    // Delete a Person by their ID
    public void delete(long id) {
        personJpaRepository.deleteById(id);
    }

    // Set default values (e.g., role) for Persons who are missing certain attributes
    public void defaults(String password, String roleName) {
        for (Person person : listAll()) {
            if (person.getPasswordHash() == null || person.getPasswordHash().isEmpty() || person.getPasswordHash().isBlank()) {
                System.out.println("L + Bozo + Skill Issue");
            }
            if (person.getRoles().isEmpty()) {
                PersonRole role = personRoleJpaRepository.findByName(roleName);
                if (role != null) {
                    person.getRoles().add(role);
                }
            }
        }
    }

    // Save a PersonRole to the database
    public void saveRole(PersonRole role) {
        PersonRole roleObj = personRoleJpaRepository.findByName(role.getName());
        if (roleObj == null) {
            personRoleJpaRepository.save(role);
        }
    }

    // List all PersonRoles in the database
    public List<PersonRole> listAllRoles() {
        return personRoleJpaRepository.findAll();
    }

    // Find a PersonRole by its name
    public PersonRole findRole(String roleName) {
        return personRoleJpaRepository.findByName(roleName);
    }

    // Add a role to a Person
    public void addRoleToPerson(String email, String roleName) {
        Optional<Person> person = personJpaRepository.findByEmail(email);

        if (person.isPresent()) {
            PersonRole role = personRoleJpaRepository.findByName(roleName);
            if (role != null) {
                boolean addRole = true;
                for (PersonRole roleObj : person.get().getRoles()) {
                    if (roleObj.getName().equals(roleName)) {
                        addRole = false;
                        break;
                    }
                }
                if (addRole) person.get().getRoles().add(role);
            }
        }
    }
}
