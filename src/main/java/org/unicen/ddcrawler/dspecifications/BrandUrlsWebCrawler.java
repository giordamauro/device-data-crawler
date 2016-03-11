package org.unicen.ddcrawler.dspecifications;

import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.model.BulkWebCrawler;
import org.unicen.ddcrawler.util.JsoupWebCrawler;

@Component
public class BrandUrlsWebCrawler implements BulkWebCrawler<String>{
    
    @Override
    public Set<String> extractDataFrom(String url) {

        Elements brandElements = new JsoupWebCrawler(url).select("#frontpage-brands > a");
        
        return brandElements.parallelStream()
            .map(link -> link.attr("href"))
            .collect(Collectors.toSet());
    }
}
