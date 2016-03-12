package org.unicen.ddcrawler.dspecifications;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.dspecifications.domain.SpecificationFeature;

public class SpecFeaturesAdapter {

	private static final String CREATED_BY_DEFAULT_VERSION = "devicespecifications.com-1.0";
	
	private static final String BRAND_AND_MODEL_FEATURE = "Brand and model";
	private static final String BRAND_ATTRIBUTE = "Brand";
	private static final String MODEL_ATTRIBUTE = "Model";
	private static final String MODEL_ALIAS_ATTRIBUTE = "Model alias";

	private final DeviceModel model;
	private final Set<DeviceFeature> features;

	private String createdByVersion = CREATED_BY_DEFAULT_VERSION;
	
	public SpecFeaturesAdapter(Set<SpecificationFeature> specificationFeatures) {

		Objects.requireNonNull(specificationFeatures, "SpecificationFeatures cannot be null");

		SpecificationFeature modelFeature = findModelFeature(specificationFeatures);
		
		Set<SpecificationFeature> modifiableFeaturesSet = new HashSet<>(specificationFeatures);
		modifiableFeaturesSet.remove(modelFeature);

		this.model = mapFeatureToDeviceModel(modelFeature);
		this.features = mapSpecificationsToDeviceFeatures(modifiableFeaturesSet, model);
	}

	public DeviceModel getModel() {
		return model;
	}

	public Set<DeviceFeature> getFeatures() {
		return Collections.unmodifiableSet(features);
	}
	
	public String getCreatedByVersion() {
		return createdByVersion;
	}

	public void setCreatedByVersion(String createdByVersion) {
		this.createdByVersion = createdByVersion;
	}

	private SpecificationFeature findModelFeature(Set<SpecificationFeature> specificationFeatures) {
		
		SpecificationFeature modelFeature = specificationFeatures.parallelStream()
				.filter(feature -> BRAND_AND_MODEL_FEATURE.equals(feature.getFeatureName()))
				.findFirst()
			.orElseThrow(() -> new IllegalStateException(String.format("Not any Key feature for name '%s'", BRAND_AND_MODEL_FEATURE)));

		return modelFeature;
	}
	
	private DeviceModel mapFeatureToDeviceModel(SpecificationFeature modelFeature) {
		
		Map<String, String> attributes = modelFeature.getAttributes();
		
		DeviceModel deviceModel = new DeviceModel.Builder()
				.setBrand(attributes.get(BRAND_ATTRIBUTE))
				.setModel(attributes.get(MODEL_ATTRIBUTE))
				.setModelAlias(attributes.get(MODEL_ALIAS_ATTRIBUTE))
				.setCreatedBy(createdByVersion)
				.build();

		return deviceModel;
	}
	
	private Set<DeviceFeature> mapSpecificationsToDeviceFeatures(Set<SpecificationFeature> specificationFeatures, DeviceModel model) {
		
		 Set<DeviceFeature> deviceFeatures = specificationFeatures.parallelStream()
			.map(specFeature -> {
				
				DeviceFeature feature = new DeviceFeature(model, specFeature.getFeatureName(), createdByVersion);
				feature.setAttributes(specFeature.getAttributes());
				
				return feature;
			})
			.collect(Collectors.toSet());
		 
		 return deviceFeatures;
	}
}