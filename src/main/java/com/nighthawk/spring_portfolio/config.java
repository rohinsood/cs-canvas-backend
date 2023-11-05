package com.nighthawk.spring_portfolio.mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.nighthawk.spring_portfolio.mvc.auth.Person;
import com.nighthawk.spring_portfolio.mvc.auth.PersonDetailsService;

import java.util.List;

@Component
@Configuration // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired PersonDetailsService personService;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {
            Person p = new Person();
            p.setEmail("yippee@y8ipee.com");
            p.setPasswordHash("password");

            System.out.println(p.toString());

            personService.save(p);
        };
    }
}

package com.nighthawk.spring_portfolio.mvc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.nighthawk.spring_portfolio.mvc.auth.Person;
import com.nighthawk.spring_portfolio.mvc.auth.PersonDetailsService;

import java.util.List;

@Component
@Configuration // Scans Application for ModelInit Bean, this detects CommandLineRunner
public class ModelInit {  
    @Autowired PersonDetailsService personService;

    @Bean
    CommandLineRunner run() {  // The run() method will be executed after the application starts
        return args -> {
            Person p = new Person();
            p.setEmail("yippee@y8ipee.com");
            p.setPasswordHash("password");

            System.out.println(p.toString());

            personService.save(p);
        };
    }
}

