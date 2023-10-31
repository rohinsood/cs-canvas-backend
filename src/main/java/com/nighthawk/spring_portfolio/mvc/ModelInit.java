package com.nighthawk.spring_portfolio.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.nighthawk.spring_portfolio.mvc.auth.Person;
import com.nighthawk.spring_portfolio.mvc.auth.PersonJpaRepository;
import com.nighthawk.spring_portfolio.mvc.canvas.Assignment;
import com.nighthawk.spring_portfolio.mvc.canvas.AssignmentRepository;
import com.nighthawk.spring_portfolio.mvc.canvas.Submission;
import com.nighthawk.spring_portfolio.mvc.canvas.SubmissionRepository;


@Component // Scans Application for ModelInit Bean, this detects CommandLineRunner
@Configuration // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  

    @Autowired AssignmentRepository assRepo;
    @Autowired PersonJpaRepository personRepo;
    @Autowired SubmissionRepository subRepo;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {
            Person person = new Person();

            person.setAdmin(false);
            person.setEmail("rs.rohinsood@gmail.com");
            person.setPasswordHash("Qwerty123!");

            personRepo.save(person);

            Assignment ass = new Assignment();
            ass.setDescription("Bean");
            ass.setName("Bean");
            ass.setDueDate("10/30/2023 @ 10:59");
            
            Submission sub = new Submission();
            sub.setEmail(person.getEmail());
            sub.setLink("https://www.google.com");
            sub.setTime("10-23-2023");

            sub.setAssignment(ass);

            List<Submission> subList = new ArrayList<Submission>();

            subList.add(sub);

            ass.setSubmissions(subList);

            assRepo.save(ass);
            subRepo.save(sub);
        };
    }
}

