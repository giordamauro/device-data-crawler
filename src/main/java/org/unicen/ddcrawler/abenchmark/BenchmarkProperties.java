package org.unicen.ddcrawler.abenchmark;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("benchmarks")
public class BenchmarkProperties {

    private final Map<String, String> urls = new HashMap<>();

    public Map<String, String> getUrls() {
        return urls;
    }
}