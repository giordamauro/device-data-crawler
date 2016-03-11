package org.unicen.ddcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot Main Class. Holds the default Spring Boot default configuration.  
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.unicen.ddcrawler")
public class DeviceDataCrawlerApplication {
    
    /**
     * Main method used to start up the Spring Boot API.
     * @param args
     */
	public static void main(String[] args) throws Exception {
	    
	    ConfigurableApplicationContext appContext = SpringApplication.run(DeviceDataCrawlerApplication.class, args);
	    CrawlingProcessCommand crawlingProcessCommand = appContext.getBean(CrawlingProcessCommand.class);
	    
	    crawlingProcessCommand.run();
	}
}
