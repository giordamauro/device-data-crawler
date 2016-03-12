package org.unicen.ddcrawler;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.model.DeviceDataExtractor;

/**
 * 
 */
@Component
public class ExtractingProcessCommand {

    @Autowired
    private Set<DeviceDataExtractor> dataExtractors;
    
    public void run(){

    	dataExtractors.forEach(dataExtractor -> System.out.println(dataExtractor.extractDeviceData()));
    }
}
