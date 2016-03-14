package org.unicen.ddcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Main Class. Holds the default Spring Boot default configuration.  
 */
@SpringBootApplication
public class Application {
    
    /**
     * Main method used to start up the Spring Boot API.
     * @param args
     */
	public static void main(String[] args) throws Exception {
	    
		SpringApplication.run(Application.class, args);
	}
}
