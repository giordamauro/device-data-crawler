package org.unicen.ddcrawler.abenchmark;

import java.util.Objects;

public class BenchmarkFeature {

    private final String featureName;
    
    private final String brand;
    private final String model;
    private final String benchmarkValue;
    
    private BenchmarkFeature(Builder builder) {
        
        Objects.requireNonNull(builder.featureName, "FeatureName cannot be null");
        Objects.requireNonNull(builder.brand, "Brand cannot be null");
        Objects.requireNonNull(builder.model, "Model cannot be null");
        Objects.requireNonNull(builder.benchmarkValue, "BenchmarkValue cannot be null");
        
        this.featureName = builder.featureName;
        this.brand = builder.brand;
        this.model = builder.model;
        this.benchmarkValue = builder.benchmarkValue;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getBenchmarkValue() {
        return benchmarkValue;
    }

    @Override
    public String toString() {
        return "BenchmarkFeature [featureName=" + featureName + ", brand=" + brand + ", model=" + model + ", benchmarkValue=" + benchmarkValue + "]";
    }

    public static class Builder {
        
        private final String featureName;
        
        private String brand;
        private String model;
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


        public Builder setModel(String model) {
            
            Objects.requireNonNull(model, "Model cannot be null");
            this.model = model;
            
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
