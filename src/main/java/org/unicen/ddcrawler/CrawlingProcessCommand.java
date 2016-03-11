package org.unicen.ddcrawler;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.dspecifications.BrandUrlsWebCrawler;
import org.unicen.ddcrawler.dspecifications.ModelDataWebCrawler;
import org.unicen.ddcrawler.dspecifications.ModelUrlsWebCrawler;
import org.unicen.ddcrawler.dspecifications.domain.SpecificationFeature;

/**
 * 
 */
@Component
public class CrawlingProcessCommand {

    private static final Object BRAND_AND_MODEL = "Brand and model";

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
        Set<SpecificationFeature> deviceFeatures = modelDataWebCrawler.extractDataFrom(modelUrl);
        
        SpecificationFeature modelFeature = deviceFeatures.stream()
            .filter(feature -> BRAND_AND_MODEL.equals(feature.getFeatureName()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(String.format("Not any Key feature for name '%s'", BRAND_AND_MODEL)));

        System.out.println(modelFeature);
    }
}
