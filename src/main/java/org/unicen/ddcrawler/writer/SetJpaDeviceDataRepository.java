package org.unicen.ddcrawler.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceModel;

@Component
public class SetJpaDeviceDataRepository implements ItemWriter<Set<DeviceModel>> {

    private final JpaDeviceDataRepository jpaDeviceDataRepository;

    @Autowired
    public SetJpaDeviceDataRepository(JpaDeviceDataRepository jpaDeviceDataRepository) {
        this.jpaDeviceDataRepository = jpaDeviceDataRepository;
    }

    @Override
    public void write(List<? extends Set<DeviceModel>> items) throws Exception {
       
        for(Set<DeviceModel> deviceDataSet : items) {
            
            List<? extends DeviceModel> deviceDataItems = new ArrayList<>(deviceDataSet);
            jpaDeviceDataRepository.write(deviceDataItems);
        };
    }        
}