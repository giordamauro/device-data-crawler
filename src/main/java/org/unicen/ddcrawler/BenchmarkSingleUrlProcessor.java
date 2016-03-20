package org.unicen.ddcrawler;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.unicen.ddcrawler.abenchmark.BenchmarkFeaturesProcessor;
import org.unicen.ddcrawler.abenchmark.BenchmarkUrl;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.writer.JpaDeviceDataRepository;

@Configuration
public class BenchmarkSingleUrlProcessor {
        
    @Autowired
	private BenchmarkFeaturesProcessor benchmarkFeaturesProcessor;

    @Autowired
	private JpaDeviceDataRepository jpaDeviceDataRepository;

    public void processDeviceDataUrl(String featureName, String modelUrl) throws Exception {
    
    	Objects.requireNonNull(modelUrl, "Url cannot be null");
    	
    	BenchmarkUrl benchmarkUrl = new BenchmarkUrl(featureName, modelUrl);
    	Set<DeviceModel> processedModels = benchmarkFeaturesProcessor.process(benchmarkUrl);
    
		jpaDeviceDataRepository.write(new ArrayList<DeviceModel>(processedModels));
    }
    
}