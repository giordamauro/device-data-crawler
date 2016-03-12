package org.unicen.ddcrawler.model;

import java.util.Set;

import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;

public interface DeviceDataRepository {

	void storeDeviceData(DeviceModel model, Set<DeviceFeature> features);
}
