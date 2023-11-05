package com.nighthawk.spring_portfolio.mvc.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.*;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonDetailsService implements UserDetailsService {
    
    @Autowired
    private PersonJpaRepository personJpaRepository;

    // Probably don't need this
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        Optional<Person> person = personJpaRepository.findByEmail(email);
        if (!person.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        return new User(person.get().getEmail(), person.get().getPasswordHash(), authorities);
    }

    // Person stuff

    public List<Person> listAll() {
        return personJpaRepository.findAll();
    }

    public List<Person> list(String email) {
        return Arrays.asList(personJpaRepository.findByEmail(email).get());
    }

    public List<Person> listLikeNative(String term) {
        String like_term = String.format("%%%s%%", term);
        return personJpaRepository.findByLikeTermNative(like_term);
    }

    public void save(Person person) {
        person.setPasswordHash(person.getPasswordHash());
        personJpaRepository.save(person);
    }

    public Person get(long id) {
        return (personJpaRepository.findById(id).isPresent()) ? personJpaRepository.findById(id).get() : null;
    }

    public Person getByEmail(String email) {
        return personJpaRepository.findByEmail(email).get();
    }

    public void delete(long id) {
        personJpaRepository.deleteById(id);
    }

    public void defaults(String password) {
        for (Person person : listAll()) {
            if (person.getPasswordHash() == null || person.getPasswordHash().isEmpty() || person.getPasswordHash().isBlank()) {
                System.out.println("Cope ");
            }
        }
    }

}
