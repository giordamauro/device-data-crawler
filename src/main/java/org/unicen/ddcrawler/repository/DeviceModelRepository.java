package org.unicen.ddcrawler.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.unicen.ddcrawler.domain.DeviceModel;

@Repository
public interface DeviceModelRepository extends PagingAndSortingRepository<DeviceModel, Integer> {

	Optional<DeviceModel> findOneByBrandAndModel(String brand, String model);
	
	@Query(value = "SELECT * FROM device_model ORDER BY features_count DESC limit ?1 offset ?2", nativeQuery = true)
	Set<DeviceModel> findAllOrderByFeaturesCount(int limit, int offset);
}
