package org.unicen.ddcrawler.abenchmark;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceData;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;

@Component
public class BenchmarkFeaturesProcessor implements ItemProcessor<BenchmarkUrl, Set<DeviceData>> {

	private static final String CREATED_BY_DEFAULT_VERSION = "androidbenchmark.net-1.0";
	
	private String createdByVersion = CREATED_BY_DEFAULT_VERSION;
	
	@Override
	public Set<DeviceData> process(BenchmarkUrl benchmarkUrl) throws Exception {

	    BenchmarkWebCrawler benchmarkWebCrawler = new BenchmarkWebCrawler(benchmarkUrl.getFeatureName());
	    Set<BenchmarkFeature> benchmarkFeatures = benchmarkWebCrawler.extractDataFrom(benchmarkUrl.getUrl());

	    return benchmarkFeatures.parallelStream()
	            .map(benchmarkFeature -> {
	    
            		DeviceModel model = new DeviceModel.Builder()
                            .setBrand(benchmarkFeature.getBrand())
                            .setModel(benchmarkFeature.getModel())
                            .setCreatedBy(createdByVersion)
                            .build();
            		
            		DeviceFeature feature =  new DeviceFeature(model, benchmarkFeature.getFeatureName(), benchmarkFeature.getBenchmarkValue());

            		return new DeviceData(model, Collections.singleton(feature));
	            })
	            .collect(Collectors.toSet());
	}
	
	public String getCreatedByVersion() {
		return createdByVersion;
	}

	public void setCreatedByVersion(String createdByVersion) {
		this.createdByVersion = createdByVersion;
	}
}
