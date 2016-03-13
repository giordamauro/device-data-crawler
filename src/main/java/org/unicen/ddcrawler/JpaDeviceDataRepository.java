package org.unicen.ddcrawler;

import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.model.DeviceDataRepository;
import org.unicen.ddcrawler.repository.DeviceModelRepository;

@Component
public class JpaDeviceDataRepository implements DeviceDataRepository{

	private final DeviceModelRepository deviceModelRepository;

	@Autowired
	public JpaDeviceDataRepository(DeviceModelRepository deviceModelRepository) {
		this.deviceModelRepository = deviceModelRepository;
	}	

	@Override
	public void storeDeviceData(DeviceModel model, Set<DeviceFeature> features) {

		Objects.requireNonNull(model, "Model cannot be null");
		Objects.requireNonNull(features, "Features cannot be null");
		
		DeviceModel deviceModel = deviceModelRepository.findOneByBrandAndModel(model.getBrand(), model.getModel())
				.orElse(model);
		
		Set<DeviceFeature> deviceFeatures = deviceModel.getFeatures()
			.map(currentFeatures -> {
				currentFeatures.addAll(features);
				return currentFeatures;
			})
			.orElse(features);
		
		deviceModel.setFeatures(deviceFeatures);
		deviceModelRepository.save(deviceModel);
	}
}
