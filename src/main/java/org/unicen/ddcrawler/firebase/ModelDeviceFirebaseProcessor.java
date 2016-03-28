package org.unicen.ddcrawler.firebase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;

@Component
public class ModelDeviceFirebaseProcessor implements ItemProcessor<DeviceModel, FirebaseData> {

    private static final Log LOGGER = LogFactory.getLog(ModelDeviceFirebaseProcessor.class);

    private static final String DEVICES_URL_ID = "/devices/{brand}/{model}";
    
	private final FirebaseNormalizer firebaseNormalizer;
	
	@Autowired
	public ModelDeviceFirebaseProcessor(FirebaseNormalizer firebaseNormalizer) {
		this.firebaseNormalizer = firebaseNormalizer;
	}

	@Override
	public FirebaseData process(DeviceModel deviceModel) throws Exception {
	    
	    LOGGER.info("Start processing Firebase data " + deviceModel);
	    
	    String brand = firebaseNormalizer.normalizeKey(deviceModel.getBrand());
	    String model = firebaseNormalizer.normalizeKey(deviceModel.getModel());

	    String urlId = DEVICES_URL_ID
	    		.replace("{brand}", brand)
	    		.replace("{model}", model);
	    
	    Map<String, Object> data = new HashMap<>();
	    
	    Object createdBy = firebaseNormalizer.normalizeValue(deviceModel.getCreatedBy());
	    data.put("created-by", createdBy);
	    
	    Object createdOn = firebaseNormalizer.normalizeValue(deviceModel.getCreatedOn());
	    data.put("created-on", createdOn);
	    
	    deviceModel.getName()
	    	.ifPresent(name -> {
	    	    Object nameValue = firebaseNormalizer.normalizeValue(name);
	    	    data.put("name", nameValue);
	    	});


	    Map<String, Object> features = getFirebaseFeatures(deviceModel.getFeatures());
	    data.put("features", features);
	    
	    FirebaseData firebaseData = new FirebaseData(urlId, data);
	    
		return firebaseData;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getFirebaseFeatures(Set<DeviceFeature> features) {

		Map<String, Object> featuresData = new HashMap<>();
		features.forEach(feature -> {
			
			String category = firebaseNormalizer.normalizeKey(feature.getCategory());
			Map<String, Object> categoryData = (Map<String, Object>) featuresData.get(category);
			if(categoryData == null){
				categoryData = new HashMap<>();
				featuresData.put(category, categoryData);
			}
			
			String featureName = firebaseNormalizer.normalizeKey(feature.getName());
			Map<String, Object> featureData = new HashMap<>();
			
			Object value = firebaseNormalizer.normalizeValue(feature.getValue());
			Object createdOn = firebaseNormalizer.normalizeValue(feature.getCreatedOn());
			Object createdBy = firebaseNormalizer.normalizeValue(feature.getCreatedBy());
			
			featureData.put("value", value);
			featureData.put("created-on", createdOn);
			featureData.put("created-by", createdBy);

			categoryData.put(featureName, featureData);
		});
		
		return featuresData;
	}
}
