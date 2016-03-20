package org.unicen.ddcrawler.dspecifications;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceDataUrl;
import org.unicen.ddcrawler.repository.DeviceDataUrlRepository;

/**
 * 
 */
@Component
public class DSpecificationsModelUrlReader implements ItemReader<DeviceDataUrl> {

    private DeviceDataUrlRepository deviceDataUrlRepository;
	private int nextId;
    
	@Autowired
	public DSpecificationsModelUrlReader(@Value("${deviceProcess.nextId}") Integer nextId, DeviceDataUrlRepository deviceDataUrlRepository) {
		
		this.deviceDataUrlRepository = deviceDataUrlRepository;
		this.nextId = nextId;
	}

	@Override
	public DeviceDataUrl read() throws Exception {

		DeviceDataUrl deviceDataUrl = deviceDataUrlRepository.findOne(nextId);
		if(deviceDataUrl != null) {
			incrementNext();
		}
		
		return deviceDataUrl;
	}
		
	private synchronized void incrementNext() {
		nextId++;
	}
}
