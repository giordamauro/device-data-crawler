package org.unicen.ddcrawler.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.unicen.ddcrawler.domain.DeviceModel;

public interface DeviceModelRepository extends CrudRepository<DeviceModel, UUID>{

}
