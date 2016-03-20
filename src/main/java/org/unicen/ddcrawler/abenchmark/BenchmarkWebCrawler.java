package org.unicen.ddcrawler.abenchmark;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.unicen.ddcrawler.model.BulkWebCrawler;
import org.unicen.ddcrawler.util.JsoupWebCrawler;

public class BenchmarkWebCrawler implements BulkWebCrawler<BenchmarkFeature>{
    
    private final String benchmarkName;
    
    public BenchmarkWebCrawler(String benchmarkName) {
        this.benchmarkName = benchmarkName;
    }

    @Override
    public Set<BenchmarkFeature> extractDataFrom(String url) {

        Optional<Element> tableElement = new JsoupWebCrawler(url).selectOne("table.chart");
        Elements modelElements = tableElement.map(table -> table.select("a"))
        			.orElseThrow(() -> new IllegalStateException("Expected chart table on page " + url));
        
        Set<BenchmarkFeature> benchmarkFeatures = new HashSet<>();
        
		modelElements.forEach(modelElement -> {

			String brandAndModel = modelElement.text();

			String brandAndModelSplit[] = brandAndModel.split(" ");
			String brand = brandAndModelSplit[0];
			String model = brandAndModel.replaceFirst(brand, "");

			Element tableData = modelElement.parent().nextElementSibling();
			Element valueDiv = tableData.select("div").get(0);

			valueDiv.select("span").remove();

			String benchmarkValue = valueDiv.text();

			BenchmarkFeature benchmarFeature = new BenchmarkFeature.Builder(benchmarkName)
					.setBrand(brand)
					.setModel(model)
					.setBenchmarkValue(benchmarkValue)
					.build();

			benchmarkFeatures.add(benchmarFeature);
		});
		
		return benchmarkFeatures;
    }
}
