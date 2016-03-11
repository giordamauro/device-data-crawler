package org.unicen.ddcrawler.dspecifications.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DeviceFeature {

    private final String featureName;
    
    private final Map<String, String> attributes = new HashMap<>();
    
    public DeviceFeature(String featureName){
        
        Objects.requireNonNull(featureName);
        
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    public void setAttribute(String attribute, String value){
        attributes.put(attribute, value);
    }

    @Override
    public String toString() {
        return "DeviceFeature [featureName=" + featureName + ", attributes=" + attributes + "]";
    }
}
