package org.unicen.ddcrawler.dspecifications;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceDataUrl;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.dspecifications.domain.SpecificationFeature;

@Component
public class DSpecificationsProcessor implements ItemProcessor<DeviceDataUrl, DeviceModel> {

    private static final Log LOGGER = LogFactory.getLog(DSpecificationsProcessor.class);
    
	private static final String CREATED_BY_DEFAULT_VERSION = "devicespecifications.com-1.0";
	private static final int MAX_RETRIES = 3;
	
	private static final String BRAND_AND_MODEL_FEATURE = "Brand and model";
	private static final String BRAND_ATTRIBUTE = "Brand";
	private static final String MODEL_ATTRIBUTE = "Model";
	private static final String MODEL_ALIAS_ATTRIBUTE = "Model alias";

	private static final String RECENT_COMPARISONS_CATEGORY = "Most recent comparisons";
	private static final String PRICES_CATEGORY = "Prices of";


	@Autowired
	private ModelDataWebCrawler modelDataWebCrawler;

	private String createdByVersion = CREATED_BY_DEFAULT_VERSION;
	
	@Override
	public DeviceModel process(DeviceDataUrl modelUrl) throws Exception {
	    
	    LOGGER.info("Start processing DS model url " + modelUrl);
	    
	    Set<SpecificationFeature> specificationFeatures = getSpecificationFeatures(modelUrl.getModelUrl());
	    SpecificationFeature modelFeature = findModelFeature(specificationFeatures);
        
        Set<SpecificationFeature> modifiableFeaturesSet = new HashSet<>(specificationFeatures);
        modifiableFeaturesSet.remove(modelFeature);

        DeviceModel model = mapFeatureToDeviceModel(modelFeature);
        Set<DeviceFeature> features = mapSpecificationsToDeviceFeatures(modifiableFeaturesSet);
        
        model.addFeatures(features);
        
        return model;
	}
	
	public String getCreatedByVersion() {
		return createdByVersion;
	}

	public void setCreatedByVersion(String createdByVersion) {
		this.createdByVersion = createdByVersion;
	}
	
	private Set<SpecificationFeature> getSpecificationFeatures(String modelUrl) {
		
		Set<SpecificationFeature> specificationFeatures = null;
	    
	    int retries = 0;
	    while(specificationFeatures == null && retries < MAX_RETRIES){
		    try {
		    	specificationFeatures = modelDataWebCrawler.extractDataFrom(modelUrl);
		    }
		    catch(Exception e){
		    	LOGGER.error("Exception processing url " + modelUrl, e);
		    	retries++;
		    }
	    }
	    
	    if(specificationFeatures == null){
	    	throw new IllegalStateException(String.format("FAIL - Completed %s retries for url %s", retries, modelUrl));
	    }
	    
	    return specificationFeatures;
	}

	private SpecificationFeature findModelFeature(Set<SpecificationFeature> specificationFeatures) {
		
		SpecificationFeature modelFeature = specificationFeatures.parallelStream()
				.filter(feature -> BRAND_AND_MODEL_FEATURE.equals(feature.getFeatureName()))
				.findFirst()
			.orElseThrow(() -> new IllegalStateException(String.format("Not any Key feature for name '%s'", BRAND_AND_MODEL_FEATURE)));

		return modelFeature;
	}
	
	private DeviceModel mapFeatureToDeviceModel(SpecificationFeature modelFeature) {
		
		Map<String, String> attributes = modelFeature.getAttributes();
		
		DeviceModel deviceModel = new DeviceModel.Builder()
				.setBrand(attributes.get(BRAND_ATTRIBUTE))
				.setModel(attributes.get(MODEL_ATTRIBUTE))
				.setModelAlias(attributes.get(MODEL_ALIAS_ATTRIBUTE))
				.setCreatedBy(createdByVersion)
				.build();

		return deviceModel;
	}
	
	private Set<DeviceFeature> mapSpecificationsToDeviceFeatures(Set<SpecificationFeature> specificationFeatures) {

		Set<DeviceFeature> deviceFeatures = new HashSet<>();

		specificationFeatures.forEach(specFeature -> {

			String category = specFeature.getFeatureName();
			if(!category.startsWith(RECENT_COMPARISONS_CATEGORY) && !category.startsWith(PRICES_CATEGORY)){
	
				specFeature.getAttributes().forEach( (name, value) -> {
				
					DeviceFeature feature = new DeviceFeature(specFeature.getFeatureName(), name, value);
					feature.setCreatedBy(createdByVersion);
				
					deviceFeatures.add(feature);
				});
			}
		});

		return deviceFeatures;
	}
}
