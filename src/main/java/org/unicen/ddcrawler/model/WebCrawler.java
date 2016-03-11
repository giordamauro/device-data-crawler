package org.unicen.ddcrawler.model;

public interface WebCrawler<T> {
    
   T extractDataFrom(String url);
}
