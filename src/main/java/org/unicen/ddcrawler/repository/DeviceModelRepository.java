package org.unicen.ddcrawler.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.unicen.ddcrawler.domain.DeviceModel;

@Repository
public interface DeviceModelRepository extends PagingAndSortingRepository<DeviceModel, Integer> {

	Optional<DeviceModel> findOneByBrandAndModel(String brand, String model);
}
