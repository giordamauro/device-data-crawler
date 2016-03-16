package org.unicen.ddcrawler;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceData;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.repository.DeviceFeatureRepository;
import org.unicen.ddcrawler.repository.DeviceModelRepository;

@Component
public class JpaDeviceDataRepository implements ItemWriter<DeviceData> {

	private static final Log LOGGER = LogFactory.getLog(JpaDeviceDataRepository.class);
	
	private final DeviceModelRepository deviceModelRepository;
	private final DeviceFeatureRepository deviceFeatureRepository;
	
	@Autowired
	public JpaDeviceDataRepository(DeviceModelRepository deviceModelRepository, DeviceFeatureRepository deviceFeatureRepository) {
		this.deviceModelRepository = deviceModelRepository;
		this.deviceFeatureRepository = deviceFeatureRepository;
	}	

	@Override
	public void write(List<? extends DeviceData> deviceDataItems) throws Exception {
		
		deviceDataItems.forEach(deviceData -> {
			
			try {
				storeDeviceData(deviceData.getModel(), deviceData.getFeatures());
				
				LOGGER.info("Successfully stored " + deviceData);
			}
			catch(Exception e){
				
				LOGGER.error("Exception storing " + deviceData, e);
			}
		});
	}
	
	private void storeDeviceData(DeviceModel model, Optional<Set<DeviceFeature>> features) {

		DeviceModel deviceModel = deviceModelRepository.findOneByBrandAndModel(model.getBrand(), model.getModel())
		        .map(existingDevice -> {
		            
		            LOGGER.info("Found existing device matching with " + model);
		            return existingDevice;
		        })
				.orElse(deviceModelRepository.save(model));
		
		features.ifPresent(modelFeatures -> {
			Set<DeviceFeature> deviceFeatures = deviceFeatureRepository.findByModelUuid(deviceModel.getUuid());
			
			Set<DeviceFeature> missingFeatures = modelFeatures;
			if(!deviceFeatures.isEmpty()){
				missingFeatures = modelFeatures.parallelStream()
						.filter(feature -> !deviceFeatures.contains(feature))
						.collect(Collectors.toSet());
			}
	
			if(!missingFeatures.isEmpty()){
				missingFeatures.forEach(feature -> feature.setModel(deviceModel));
				deviceFeatureRepository.save(missingFeatures);
			}
		});
	}
}
