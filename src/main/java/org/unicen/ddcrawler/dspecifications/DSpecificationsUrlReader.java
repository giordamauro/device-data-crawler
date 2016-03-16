package org.unicen.ddcrawler.dspecifications;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class DSpecificationsUrlReader implements ItemReader<String> {

    private static final Log LOGGER = LogFactory.getLog(DSpecificationsUrlReader.class);
    
	private final ModelUrlsWebCrawler modelsWebCrawler;
	
	private final Iterator<String> nextBrand;
	private Iterator<String> nextModel;
	
	@Autowired
	public DSpecificationsUrlReader(@Value("${deviceSpecifications.url}") String deviceSpecificationsUrl, BrandUrlsWebCrawler brandsWebCrawler, ModelUrlsWebCrawler modelsWebCrawler) {
		
		this.modelsWebCrawler = modelsWebCrawler;
		
		Set<String> brandUrls = brandsWebCrawler.extractDataFrom(deviceSpecificationsUrl);
		this.nextBrand = brandUrls.iterator();

		this.nextModel = readNextBrandModels();
	}

	@Override
	public String read() throws Exception {
		
		if(nextModel.hasNext()){
			return nextModel.next();
		}
		
		if(nextBrand.hasNext()) {
			this.nextModel = readNextBrandModels();
			return nextModel.next();
		}

		return null;
	}
		
	private Iterator<String> readNextBrandModels() {
		
	    String brandUrl = nextBrand.next();
	    
	    try {
	    	Set<String> currentModelUrls = modelsWebCrawler.extractDataFrom(brandUrl);
    		return currentModelUrls.iterator();
		
	    } catch(Exception e){
	    
	        LOGGER.error("Exception reading brand url " + brandUrl, e);
	        return readNextBrandModels();
	    }
	}		
}
