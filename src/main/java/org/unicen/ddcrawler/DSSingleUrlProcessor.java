package org.unicen.ddcrawler;


import java.util.Collections;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.unicen.ddcrawler.domain.DeviceDataUrl;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.dspecifications.DSpecificationsProcessor;
import org.unicen.ddcrawler.writer.JpaDeviceDataRepository;

@Configuration
public class DSSingleUrlProcessor {
        
    @Autowired
	private DSpecificationsProcessor specificationsDataProcessor;

    @Autowired
	private JpaDeviceDataRepository jpaDeviceDataRepository;

    public void processDeviceDataUrl(String modelUrl) throws Exception {
    
    	Objects.requireNonNull(modelUrl, "Url cannot be null");
    	
    	DeviceDataUrl deviceDataUrl = new DeviceDataUrl(modelUrl);
		DeviceModel processedModel = specificationsDataProcessor.process(deviceDataUrl);
    
		jpaDeviceDataRepository.write(Collections.singletonList(processedModel));
    }
    
}