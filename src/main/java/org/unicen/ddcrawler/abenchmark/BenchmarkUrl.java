package org.unicen.ddcrawler.abenchmark;

import java.util.Objects;

public class BenchmarkUrl {

    private final String featureName;
    private final String url;

    public BenchmarkUrl(String featureName, String url) {
        
        Objects.requireNonNull(featureName, "FeatureName cannot be null");
        Objects.requireNonNull(url, "Url cannot be null");
        
        this.featureName = featureName;
        this.url = url;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "BenchmarkUrl [featureName=" + featureName + ", url=" + url + "]";
    }
}
