package org.unicen.ddcrawler.model;

@FunctionalInterface
public interface WebCrawler<T> {
    
   T extractDataFrom(String url);
}
