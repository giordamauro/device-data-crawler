package org.unicen.ddcrawler.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unicen.ddcrawler.domain.DeviceFeature;

@Repository
public interface DeviceFeatureRepository extends CrudRepository<DeviceFeature, UUID> {
	
	Set<DeviceFeature> findByModelUuid(UUID uuid);
	
	Optional<DeviceFeature> findOneByFeatureNameAndModelUuid(String featureName, UUID uuid);
}
