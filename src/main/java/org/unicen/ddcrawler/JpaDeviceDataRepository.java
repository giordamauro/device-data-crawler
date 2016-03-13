package org.unicen.ddcrawler;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.model.DeviceDataRepository;
import org.unicen.ddcrawler.repository.DeviceFeatureRepository;
import org.unicen.ddcrawler.repository.DeviceModelRepository;

@Component
public class JpaDeviceDataRepository implements DeviceDataRepository{

	private final DeviceModelRepository deviceModelRepository;
	private final DeviceFeatureRepository deviceFeatureRepository;
	
	@Autowired
	public JpaDeviceDataRepository(DeviceModelRepository deviceModelRepository, DeviceFeatureRepository deviceFeatureRepository) {
		this.deviceModelRepository = deviceModelRepository;
		this.deviceFeatureRepository = deviceFeatureRepository;
	}	

	@Override
	public void storeDeviceData(DeviceModel model, Set<DeviceFeature> features) {

		Objects.requireNonNull(model, "Model cannot be null");
		Objects.requireNonNull(features, "Features cannot be null");
		
		DeviceModel deviceModel = deviceModelRepository.findOneByBrandAndModel(model.getBrand(), model.getModel())
				.orElse(deviceModelRepository.save(model));
		
		Set<DeviceFeature> missingFeatures = deviceModel.getFeatures()
			.map(deviceFeatures -> features.parallelStream()
					.filter(feature -> !deviceFeatures.contains(feature))
					.collect(Collectors.toSet()))
			.orElse(features);

		if(!missingFeatures.isEmpty()){
			missingFeatures.forEach(feature -> feature.setModel(deviceModel));
			
			deviceFeatureRepository.save(missingFeatures);
			deviceModel.addFeatures(missingFeatures);
		}
	}
}
