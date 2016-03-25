package org.unicen.ddcrawler.gsupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.domain.ModelDeviceNormalizer;

@Component
public class GSupportedDevicesProcessor implements ItemProcessor<AndroidDevice, DeviceModel> {

    private static final Log LOGGER = LogFactory.getLog(GSupportedDevicesProcessor.class);
    
	private static final String CREATED_BY_DEFAULT_VERSION = "google-supported-devices";

	private String createdByVersion = CREATED_BY_DEFAULT_VERSION;
	
	private final ModelDeviceNormalizer modelDeviceNormalizer;
	
	@Autowired
	public GSupportedDevicesProcessor(ModelDeviceNormalizer modelDeviceNormalizer) {
		this.modelDeviceNormalizer = modelDeviceNormalizer;
	}

	@Override
	public DeviceModel process(AndroidDevice androidDevice) throws Exception {
	    
	    LOGGER.info("Start processing Google supported " + androidDevice);
	    
	    String brand = androidDevice.getRetailBranding();
	    
	    if(brand == null || brand.isEmpty()){
	    	LOGGER.info("Ignoring empty brand device: " + androidDevice);
	    	return null;
	    }
	    	
	    brand = modelDeviceNormalizer.getNormalizedBrand(brand);
	    String model = modelDeviceNormalizer.getNormalizedModel(brand, androidDevice.getModel());
	    
	    DeviceModel deviceModel = new DeviceModel.Builder()
				.setBrand(brand)
				.setModel(model)
				.setName(androidDevice.getMarketingName())
				.setCreatedBy(createdByVersion)
				.build();

	    return deviceModel;
	}
	
	public String getCreatedByVersion() {
		return createdByVersion;
	}

	public void setCreatedByVersion(String createdByVersion) {
		this.createdByVersion = createdByVersion;
	}
}
