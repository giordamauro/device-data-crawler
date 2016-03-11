package org.unicen.ddcrawler.dspecifications;

import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.unicen.ddcrawler.dspecifications.domain.SpecificationFeature;
import org.unicen.ddcrawler.model.WebCrawler;
import org.unicen.ddcrawler.util.JsoupWebCrawler;

@Component
public class ModelDataWebCrawler implements WebCrawler<Set<SpecificationFeature>>{
    
    @Override
    public Set<SpecificationFeature> extractDataFrom(String url) {

        Elements sectionHeaders = new JsoupWebCrawler(url).select(".section-header > h2");
        
        return sectionHeaders.parallelStream()
            .map(headerElement -> {
             
                SpecificationFeature deviceFeature = new SpecificationFeature(headerElement.text());
                
                Element table = headerElement.parent().nextElementSibling();
                Elements tableRows = table.select("tbody > tr");
                
                tableRows.forEach(tableRow -> {
                
                    Elements tData = tableRow.select("td");
                    Element attribute = tData.get(0);
                    Element value = tData.get(1);
                    
                    attribute.select("p").remove();
                    
                    deviceFeature.setAttribute(attribute.text(), value.text());
                });
                
                return deviceFeature;
            })
            .collect(Collectors.toSet());
    }
}
