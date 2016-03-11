package org.unicen.ddcrawler;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.dspecifications.BrandUrlsWebCrawler;
import org.unicen.ddcrawler.dspecifications.ModelDataWebCrawler;
import org.unicen.ddcrawler.dspecifications.ModelUrlsWebCrawler;
import org.unicen.ddcrawler.dspecifications.domain.DeviceFeature;

/**
 * 
 */
@Component
public class CrawlingProcessCommand {

    private final String deviceSpecificationsUrl;
    
    @Autowired
    private BrandUrlsWebCrawler brandsWebCrawler;
    
    @Autowired
    private ModelUrlsWebCrawler modelsWebCrawler;
    
    @Autowired
    private ModelDataWebCrawler modelDataWebCrawler;
    
    @Autowired
    public CrawlingProcessCommand(@Value("${urls.deviceSpecifications}") String deviceSpecificationsUrl){
        
        this.deviceSpecificationsUrl = deviceSpecificationsUrl;
    }
    
    public void run(){
        
        Set<String> brandUrls = brandsWebCrawler.extractDataFrom(deviceSpecificationsUrl);
        String brandUrl = brandUrls.iterator().next();
        
        Set<String> modelUrls = modelsWebCrawler.extractDataFrom(brandUrl);
        
        String modelUrl = modelUrls.iterator().next();
        Set<DeviceFeature> deviceFeatures = modelDataWebCrawler.extractDataFrom(modelUrl);
        
        deviceFeatures.forEach(feature -> System.out.println(feature.getFeatureName()));
    }
}
