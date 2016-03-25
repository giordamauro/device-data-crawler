package org.unicen.ddcrawler.repository;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceDataUrl;

@Component
public class JpaDeviceUrlRepository implements ItemWriter<DeviceDataUrl> {

	private static final Log LOGGER = LogFactory.getLog(JpaDeviceUrlRepository.class);
	
	private final DeviceDataUrlRepository deviceDataUrlRepository;
	
	@Autowired
	public JpaDeviceUrlRepository(DeviceDataUrlRepository deviceDataUrlRepository) {
		this.deviceDataUrlRepository = deviceDataUrlRepository;
	}	

	@Override
	public void write(List<? extends DeviceDataUrl> deviceUrlItems) throws Exception {
		
		deviceUrlItems.forEach(deviceUrl -> {
			
			try {
				deviceDataUrlRepository.save(deviceUrl);
				
				LOGGER.info("Successfully stored " + deviceUrl);
			}
			catch(Exception e){
				
				LOGGER.error("Exception storing " + deviceUrl, e);
			}
		});
	}
}
