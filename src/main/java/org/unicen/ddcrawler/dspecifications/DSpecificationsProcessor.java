package org.unicen.ddcrawler.dspecifications;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceDataUrl;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.domain.ModelDeviceNormalizer;
import org.unicen.ddcrawler.dspecifications.domain.SpecificationFeature;

@Component
public class DSpecificationsProcessor implements ItemProcessor<DeviceDataUrl, Set<DeviceModel>> {

    private static final Log LOGGER = LogFactory.getLog(DSpecificationsProcessor.class);
    
	private static final String CREATED_BY_DEFAULT_VERSION = "devicespecifications.com-1.0";
	private static final int MAX_RETRIES = 3;
	
	private static final String BRAND_AND_MODEL_FEATURE = "Brand and model";
	private static final String BRAND_ATTRIBUTE = "Brand";
	private static final String MODEL_ATTRIBUTE = "Model";
	private static final String MODEL_ALIAS_ATTRIBUTE = "Model alias";

	private static final String RECENT_COMPARISONS_CATEGORY = "Most recent comparisons";
	private static final String PRICES_CATEGORY = "Prices of";
	private static final String LAST_VIEWED_CATEGORY = "Last viewed devices";

	@Autowired
	private ModelDataWebCrawler modelDataWebCrawler;

	@Autowired
	private ModelDeviceNormalizer modelDeviceNormalizer;
	
	private String createdByVersion = CREATED_BY_DEFAULT_VERSION;
	
	@Override
	public Set<DeviceModel> process(DeviceDataUrl modelUrl) throws Exception {
	    
	    LOGGER.info("Start processing DS model url " + modelUrl);
	    
	    Set<SpecificationFeature> specificationFeatures = getSpecificationFeatures(modelUrl.getModelUrl());
	    SpecificationFeature modelFeature = findModelFeature(specificationFeatures);
        
        Set<SpecificationFeature> modifiableFeaturesSet = new HashSet<>(specificationFeatures);
        modifiableFeaturesSet.remove(modelFeature);

        Set<DeviceModel> models = mapFeatureToDeviceModels(modelFeature, modelUrl.getModelUrl());
        Set<DeviceFeature> features = mapSpecificationsToDeviceFeatures(modifiableFeaturesSet);
        
        if(models.size() == 1){
        	DeviceModel model = models.iterator().next();
        	model.addFeatures(features);
        }
        else{
	        models.forEach(model -> {
	        	
	        	Set<DeviceFeature> copyOfFeatures = features.parallelStream()
	        			.map(feature -> {
	        				DeviceFeature deviceFeature = new DeviceFeature(feature.getCategory(), feature.getName(), feature.getValue());
	        				deviceFeature.setCreatedBy(feature.getCreatedBy());
	        				
	        				return deviceFeature;
	        			})
	        			.collect(Collectors.toSet());
	        	
	        	model.addFeatures(copyOfFeatures);
	        });
        }
        return models;
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
	
	private Set<DeviceModel> mapFeatureToDeviceModels(SpecificationFeature modelFeature, String url) {
		
		Map<String, List<String>> attributes = modelFeature.getAttributes();
		
		final String brand = modelDeviceNormalizer.getNormalizedBrand(attributes.get(BRAND_ATTRIBUTE).get(0));
		final String modelAttribute = attributes.get(MODEL_ATTRIBUTE).get(0);

		Set<String> models = new HashSet<>();
		models.add(modelAttribute);
				
		Optional.ofNullable(attributes.get(MODEL_ALIAS_ATTRIBUTE))
			.ifPresent(modelAlias -> models.addAll(modelAlias));

		return models.parallelStream()
			.map(alias -> modelDeviceNormalizer.getNormalizedModel(brand, alias))
			.map(model -> new DeviceModel.Builder()
					.setBrand(brand)
					.setModel(model)
					.setCreatedBy(url)
					.build())
			.collect(Collectors.toSet());
	}
	
	private Set<DeviceFeature> mapSpecificationsToDeviceFeatures(Set<SpecificationFeature> specificationFeatures) {

		Set<DeviceFeature> deviceFeatures = new HashSet<>();

		specificationFeatures.forEach(specFeature -> {

			String category = specFeature.getFeatureName();
			if(!category.startsWith(RECENT_COMPARISONS_CATEGORY) && !category.startsWith(PRICES_CATEGORY) && !category.startsWith(LAST_VIEWED_CATEGORY)){
	
				specFeature.getAttributes().forEach( (name, values) -> {
				
					String value = values.stream().collect(Collectors.joining(", "));
					DeviceFeature feature = new DeviceFeature(specFeature.getFeatureName(), name, value);
					feature.setCreatedBy(createdByVersion);
				
					deviceFeatures.add(feature);
				});
			}
		});

		return deviceFeatures;
	}
}
