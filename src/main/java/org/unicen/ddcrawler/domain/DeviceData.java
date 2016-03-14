package org.unicen.ddcrawler.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class DeviceData {

	private final DeviceModel model;
	private final Set<DeviceFeature> features;
	
	public DeviceData(DeviceModel model, Set<DeviceFeature> features) {
	
		Objects.requireNonNull(model, "Model cannot be null");
		Objects.requireNonNull(features, "Features cannot be null");
		
		this.model = model;
		this.features = features;
	}

	public DeviceModel getModel() {
		return model;
	}

	public Optional<Set<DeviceFeature>> getFeatures() {
		return Optional.ofNullable(features);
	}

	@Override
	public String toString() {
		return "DeviceData [model=" + model + ", features=" + features + "]";
	}
}
