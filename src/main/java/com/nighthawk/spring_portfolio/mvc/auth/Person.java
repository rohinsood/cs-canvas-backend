package com.nighthawk.spring_portfolio.mvc.auth;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

// sets attributes for each person - id, roles, email, password which is stored with a hash
public class Person {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // For roles
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<PersonRole> roles = new ArrayList<>();

    @NotEmpty
    @Size(min = 5)
    @Column(unique = true)
    @Email
    String email;

    String passwordHash;

    // True if user is admin, false otherwise
    boolean admin;

    // main method for testing create person
    public static void main(String[] args) {
        // Create a new instance of the Person class
        Person p = new Person();

        // Set the email for the person
        p.setEmail("yippee@y8ipee.com");

        // Set the password hash for the person
        p.setPasswordHash("password");

        // Print the string representation of the person object
        System.out.println(p.toString());
    }
}
