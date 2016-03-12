
package org.unicen.ddcrawler.model;

import java.util.Map;
import java.util.Set;

import org.unicen.ddcrawler.domain.DeviceFeature;
import org.unicen.ddcrawler.domain.DeviceModel;

public interface DeviceDataExtractor {

	Map<DeviceModel, Set<DeviceFeature>> extractDeviceData();
}
