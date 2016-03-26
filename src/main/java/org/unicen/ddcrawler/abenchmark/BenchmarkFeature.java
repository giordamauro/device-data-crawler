package org.unicen.ddcrawler.abenchmark;

import java.util.Objects;
import java.util.Set;

public class BenchmarkFeature {

    private final String featureName;
    
    private final String brand;
    private final Set<String> models;
    private final String benchmarkValue;
    
    private BenchmarkFeature(Builder builder) {
        
        Objects.requireNonNull(builder.featureName, "FeatureName cannot be null");
        Objects.requireNonNull(builder.brand, "Brand cannot be null");
        Objects.requireNonNull(builder.models, "Models cannot be null");
        Objects.requireNonNull(builder.benchmarkValue, "BenchmarkValue cannot be null");
        
        this.featureName = builder.featureName;
        this.brand = builder.brand;
        this.models = builder.models;
        this.benchmarkValue = builder.benchmarkValue;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getBrand() {
        return brand;
    }

    public Set<String> getModels() {
        return models;
    }

    public String getBenchmarkValue() {
        return benchmarkValue;
    }

    @Override
    public String toString() {
        return "BenchmarkFeature [featureName=" + featureName + ", brand=" + brand + ", models=" + models + ", benchmarkValue=" + benchmarkValue + "]";
    }

    public static class Builder {
        
        private final String featureName;
        
        private String brand;
        private Set<String> models;
        private String benchmarkValue;
        
        
        public Builder(String featureName) {
            
            Objects.requireNonNull(featureName, "FeatureName cannot be null");
            
            this.featureName = featureName;
        }


        public Builder setBrand(String brand) {
            
            Objects.requireNonNull(brand, "Brand cannot be null");
            this.brand = brand;
            
            return this;
        }


        public Builder setModels(Set<String> models) {
            
            Objects.requireNonNull(models, "Models cannot be null");
            this.models = models;
            
            return this;
        }


        public Builder setBenchmarkValue(String benchmarkValue) {
            
            Objects.requireNonNull(benchmarkValue, "BenchmarkValue cannot be null");
            this.benchmarkValue = benchmarkValue;
            
            return this;
        }
        
        public BenchmarkFeature build(){
            return new BenchmarkFeature(this);
        }
    }
}
