package org.unicen.ddcrawler;

import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.model.DeviceDataExtractor;
import org.unicen.ddcrawler.model.DeviceDataRepository;

/**
 * 
 */
@Component
public class ExtractingProcessCommand {

	private static final Log LOGGER = LogFactory.getLog(ExtractingProcessCommand.class);

	@Autowired
	private Set<DeviceDataExtractor> dataExtractors;

	@Autowired
	private DeviceDataRepository dataRepository;

	public void run() {

		dataExtractors.forEach(dataExtractor -> {

			Map<DeviceModel, Set<DeviceFeature>> extractedDeviceData = dataExtractor.extractDeviceData();

			extractedDeviceData.forEach((model, features) -> {

				try {
					dataRepository.storeDeviceData(model, features);
				
				} catch (Exception e) {
					LOGGER.error(String.format("Exception storing %s + %s", model, features), e);
				}
			});
		});
	}
}
