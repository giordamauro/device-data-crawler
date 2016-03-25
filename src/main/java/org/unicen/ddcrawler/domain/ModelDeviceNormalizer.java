package org.unicen.ddcrawler.domain;

import org.springframework.stereotype.Component;

@Component
public class ModelDeviceNormalizer {

	public String getNormalizedBrand(String brand){
	
	    if(brand == null || brand.isEmpty()){
	    	throw new IllegalArgumentException("Brand cannot be null or empty");
	    }
	    	
	    return brand.toLowerCase().replaceAll(" ", "-");
	}
	
	public String getNormalizedModel(String brand, String model){
		
		if(model == null || model.isEmpty()){
	    	throw new IllegalArgumentException("Model cannot be null or empty");
	    }
		
		String deviceBrand = getNormalizedBrand(brand);
		String deviceModel = model.toLowerCase().replaceAll(" ", "-");
	    
	    if(deviceModel.startsWith(deviceBrand + "-")){
	    	deviceModel = deviceModel.replaceFirst(deviceBrand + "-", "");
	    }
	    
	    return deviceModel;
	}
}
