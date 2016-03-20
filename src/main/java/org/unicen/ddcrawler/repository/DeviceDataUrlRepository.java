package org.unicen.ddcrawler.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.unicen.ddcrawler.domain.DeviceDataUrl;

@Repository
public interface DeviceDataUrlRepository extends PagingAndSortingRepository<DeviceDataUrl, Integer> {

}
