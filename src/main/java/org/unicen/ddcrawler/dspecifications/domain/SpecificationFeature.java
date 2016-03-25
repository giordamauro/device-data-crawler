package org.unicen.ddcrawler.dspecifications.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SpecificationFeature {

    private final String featureName;
    
    private final Map<String, List<String>> attributes = new HashMap<>();
    
    public SpecificationFeature(String featureName){
        
        Objects.requireNonNull(featureName);
        
        this.featureName = featureName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }
    
    public void setAttribute(String attribute, List<String> values){
        attributes.put(attribute, values);
    }

    @Override
    public String toString() {
        return "SpecificationFeature [featureName=" + featureName + ", attributes=" + attributes + "]";
    }
}
