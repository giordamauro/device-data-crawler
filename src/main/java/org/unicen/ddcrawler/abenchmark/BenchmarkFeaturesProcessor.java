package org.unicen.ddcrawler.abenchmark;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.domain.ModelDeviceNormalizer;

@Component
public class BenchmarkFeaturesProcessor implements ItemProcessor<BenchmarkUrl, Set<DeviceModel>> {

	private static final Log LOGGER = LogFactory.getLog(BenchmarkFeaturesProcessor.class);
	
	private static final String CREATED_BY_DEFAULT_VERSION = "androidbenchmark.net-1.0";
	private static final int MAX_RETRIES = 3;

	private static final String BENCHMARK_CATEGORY = "Benchmarks";

	@Autowired
	private ModelDeviceNormalizer modelDeviceNormalizer;
	
	private String createdByVersion = CREATED_BY_DEFAULT_VERSION;
	
	@Override
	public Set<DeviceModel> process(BenchmarkUrl benchmarkUrl) throws Exception {

	    BenchmarkWebCrawler benchmarkWebCrawler = new BenchmarkWebCrawler(benchmarkUrl.getFeatureName());
	    Set<BenchmarkFeature> benchmarkFeatures = getBenchmarkFeatures(benchmarkWebCrawler, benchmarkUrl.getUrl());

	    Set<DeviceModel> deviceModels = new HashSet<>();
	    benchmarkFeatures.forEach(benchmarkFeature -> {
	    
	    	Set<String> models = benchmarkFeature.getModels();
	    	models.forEach(model -> {
	    	
	    		String normalizedBrand = modelDeviceNormalizer.getNormalizedBrand(benchmarkFeature.getBrand());
	    		String normalizedModel = modelDeviceNormalizer.getNormalizedModel(normalizedBrand, model);
	    		
	    		DeviceModel deviceModel = new DeviceModel.Builder()
	    				.setBrand(normalizedBrand)
                        .setModel(normalizedModel)
                        .setCreatedBy(benchmarkUrl.getUrl())
                        .build();
            		
            	DeviceFeature feature =  new DeviceFeature(BENCHMARK_CATEGORY, benchmarkFeature.getFeatureName(), benchmarkFeature.getBenchmarkValue());
            	feature.setCreatedBy(createdByVersion);
            	deviceModel.addFeatures(Collections.singleton(feature));
            	
            	deviceModels.add(deviceModel);
	    	});
        });
	    
	    return deviceModels;
	}
	
	public String getCreatedByVersion() {
		return createdByVersion;
	}

	public void setCreatedByVersion(String createdByVersion) {
		this.createdByVersion = createdByVersion;
	}
	
	private Set<BenchmarkFeature> getBenchmarkFeatures(BenchmarkWebCrawler benchmarkWebCrawler, String benchmarkUrl) {
		
		LOGGER.info("Start processing Benchmark url " + benchmarkUrl);
		
		Set<BenchmarkFeature> benchmarkFeatures = null;
	    
	    int retries = 0;
	    while(benchmarkFeatures == null && retries < MAX_RETRIES) {
		    try {
		    	benchmarkFeatures = benchmarkWebCrawler.extractDataFrom(benchmarkUrl);
		    }
		    catch(Exception e){
		    	LOGGER.error("Exception processing url " + benchmarkUrl, e);
		    	retries++;
		    }
	    }
	    
	    if(benchmarkFeatures == null){
	    	throw new IllegalStateException(String.format("FAIL - Completed %s retries for url %s", retries, benchmarkUrl));
	    }
	    
	    return benchmarkFeatures;
	}
}
