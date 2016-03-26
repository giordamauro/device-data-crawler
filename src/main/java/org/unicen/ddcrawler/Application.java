package org.unicen.ddcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

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
	    
		ApplicationContext appContext = SpringApplication.run(Application.class, args);

//		Process 1 DeviceSpecifications URL manually: 
//		processDSSingleUrl(appContext);
		
//		Process 1 Android Benchmarks URL manually: 
		processBenchmarkSingleUrl(appContext);
	}
	
	@SuppressWarnings("unused")
	private static void processDSSingleUrl(ApplicationContext appContext) throws Exception{

		final String modelUrl = "http://www.devicespecifications.com/en/model/f1f235cb";
		
		DSSingleUrlProcessor singleUrlProcessor = appContext.getBean(DSSingleUrlProcessor.class);
		singleUrlProcessor.processDeviceDataUrl(modelUrl);
	}
	
	@SuppressWarnings("unused")
	private static void processBenchmarkSingleUrl(ApplicationContext appContext) throws Exception{

		final String featureName = "MemoryMark";
		final String modelUrl = "http://www.androidbenchmark.net/memmark_chart.html";
		
		BenchmarkSingleUrlProcessor singleUrlProcessor = appContext.getBean(BenchmarkSingleUrlProcessor.class);
		singleUrlProcessor.processDeviceDataUrl(featureName, modelUrl);
	}
}
