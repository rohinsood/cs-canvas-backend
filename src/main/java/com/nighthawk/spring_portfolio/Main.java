package com.nighthawk.spring_portfolio;

import java.io.FileNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

// @SpringBootApplication annotation is key to building web applications using Java https://spring.io/projects/spring-boot
@SpringBootApplication
public class Main {

    // Starts a spring application as a stand-alone application from the main method
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void applicationStart() {
        System.out.println("Application started!");
        System.out.println("Admin Key: " + System.getenv("ADMIN_KEY"));

        Resource coolDbResource = new FileSystemResource("volumes/sqlite.db");

        // Check if the file exists
        if (coolDbResource.exists()) {
            System.out.println("sqlite.db file found");
        }
    }

}
