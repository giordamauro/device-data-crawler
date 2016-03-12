package org.unicen.ddcrawler.dspecifications;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.dspecifications.domain.SpecificationFeature;
import org.unicen.ddcrawler.model.DeviceDataExtractor;

/**
 * 
 */
@Component
public class DSpecificationsDataExtractor implements DeviceDataExtractor {

	private final String deviceSpecificationsUrl;

	@Autowired
	private BrandUrlsWebCrawler brandsWebCrawler;

	@Autowired
	private ModelUrlsWebCrawler modelsWebCrawler;

	@Autowired
	private ModelDataWebCrawler modelDataWebCrawler;

	@Autowired
	public DSpecificationsDataExtractor(@Value("${urls.deviceSpecifications}") String deviceSpecificationsUrl) {

		this.deviceSpecificationsUrl = deviceSpecificationsUrl;
	}

	@Override
	public Map<DeviceModel, Set<DeviceFeature>> extractDeviceData() {

		Map<DeviceModel, Set<DeviceFeature>> data = new HashMap<>();
		
		Set<String> brandUrls = brandsWebCrawler.extractDataFrom(deviceSpecificationsUrl);
		String brandUrl = brandUrls.iterator().next();

		Set<String> modelUrls = modelsWebCrawler.extractDataFrom(brandUrl);

		String modelUrl = modelUrls.iterator().next();
		Set<SpecificationFeature> deviceFeatures = modelDataWebCrawler.extractDataFrom(modelUrl);

		SpecFeaturesAdapter specFeaturesAdapter = new SpecFeaturesAdapter(deviceFeatures);
		data.put(specFeaturesAdapter.getModel(), specFeaturesAdapter.getFeatures());
		
		return data;
	}
}
