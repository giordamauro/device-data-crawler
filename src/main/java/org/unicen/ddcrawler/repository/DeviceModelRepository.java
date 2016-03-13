package org.unicen.ddcrawler.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unicen.ddcrawler.domain.DeviceModel;

@Repository
public interface DeviceModelRepository extends CrudRepository<DeviceModel, UUID> {

	Optional<DeviceModel> findOneByBrandAndModel(String brand, String model);
}
