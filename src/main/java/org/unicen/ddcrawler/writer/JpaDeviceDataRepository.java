package org.unicen.ddcrawler.writer;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.repository.DeviceModelRepository;

@Component
public class JpaDeviceDataRepository implements ItemWriter<DeviceModel> {

	private static final Log LOGGER = LogFactory.getLog(JpaDeviceDataRepository.class);
	
	private final DeviceModelRepository deviceModelRepository;
	
	@Autowired
	public JpaDeviceDataRepository(DeviceModelRepository deviceModelRepository) {
		this.deviceModelRepository = deviceModelRepository;
	}	

	@Override
	public void write(List<? extends DeviceModel> deviceDataItems) throws Exception {
		
		deviceDataItems.forEach(deviceModel -> {
			
			try {
				DeviceModel persistedModel = storeDeviceData(deviceModel);
				
				LOGGER.info("Successfully stored " + persistedModel);
			}
			catch(Exception e){
				
				LOGGER.error("Exception storing " + deviceModel, e);
			}
		});
	}
	
	private DeviceModel storeDeviceData(DeviceModel model) {

		DeviceModel deviceModel = deviceModelRepository.findOneByBrandAndModel(model.getBrand(), model.getModel())
		        .map(existingDevice -> {
		            
		            LOGGER.info(String.format("Found existing device %s matching with %s", existingDevice, model));
		            return existingDevice;
		        })
				.orElse(model);
		
		Optional.ofNullable(model.getFeatures())
			.ifPresent(modelFeatures -> deviceModel.addFeatures(modelFeatures));
					
		return deviceModelRepository.save(deviceModel);
	}
}
