package com.nighthawk.spring_portfolio.mvc.auth;

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
        Person p = new Person();
        p.setEmail("yippee@y8ipee.com");
        p.setPasswordHash("password");

        System.out.println(p.toString());
    }
}
