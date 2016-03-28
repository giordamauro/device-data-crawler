package org.unicen.ddcrawler.writer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceDataUrl;
import org.unicen.ddcrawler.repository.DeviceDataUrlRepository;

@Component
public class JpaDeviceUrlWriter implements ItemWriter<DeviceDataUrl> {

	private static final Log LOGGER = LogFactory.getLog(JpaDeviceUrlWriter.class);
	
	private final DeviceDataUrlRepository deviceDataUrlRepository;
	
	@Autowired
	public JpaDeviceUrlWriter(DeviceDataUrlRepository deviceDataUrlRepository) {
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
