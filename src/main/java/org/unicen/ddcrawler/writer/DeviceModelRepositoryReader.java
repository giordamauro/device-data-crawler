package org.unicen.ddcrawler.writer;

import java.util.Iterator;
import java.util.Set;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.repository.DeviceModelRepository;

/**
 * 
 */
@Component
public class DeviceModelRepositoryReader implements ItemReader<DeviceModel> {

	@Autowired
	private DeviceModelRepository repository;
	
	private int pageSize = 10;
	private Set<DeviceModel> itemsSet;

	private int nextPage = 0;
	private Iterator<DeviceModel> itemsIterator;
    
	@Override
	public DeviceModel read() throws Exception {
		
		if(itemsIterator == null || !itemsIterator.hasNext()){
		
			itemsSet = repository.findAllOrderByFeaturesCount(pageSize, pageSize * nextPage);

			if(itemsSet.isEmpty()){
				return null;
			}
			itemsIterator = itemsSet.iterator();
			nextPage++;
		}
		
		return itemsIterator.next();
	}
	
    public void setPageSize(int pageSize){
    	this.pageSize = pageSize;
    }

}
