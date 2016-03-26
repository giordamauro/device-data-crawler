package org.unicen.ddcrawler.abenchmark;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

			String model = brandAndModel.replaceFirst(brand + " ", "");
			Set<String> models = extractModels(model, modelElement);

			Element tableData = modelElement.parent().nextElementSibling();
			Element valueDiv = tableData.select("div").get(0);

			valueDiv.select("span").remove();

			String benchmarkValue = valueDiv.text();
			
			BenchmarkFeature benchmarFeature = new BenchmarkFeature.Builder(benchmarkName)
					.setBrand(brand)
					.setModels(models)
					.setBenchmarkValue(benchmarkValue)
					.build();

			benchmarkFeatures.add(benchmarFeature);
		});
		
		return benchmarkFeatures;
    }
    
    private Set<String> extractModels(String model, Element modelElement) {
    	
    	Set<String> models = new HashSet<>();
    	
    	String betweenParenthesis = Optional.of(model.indexOf(" ("))
			.filter(indexOfP -> indexOfP != -1)
			.map(indexOfP -> model.substring(indexOfP + 2, model.indexOf(")")))
			.orElse(null);
		
    	String baseModel = model;
    	if(betweenParenthesis != null){
    		baseModel = model.replace(" (" + betweenParenthesis + ")", "");
    		
    		if(!betweenParenthesis.equals("Various Models")){
    			models.add(betweenParenthesis);
    		}
    		else{
    			List<String> variousModels = extractVariousModels(modelElement);
    			models.addAll(variousModels);
    		}
    	}
    	models.add(baseModel);
    	
    	return models;
    }
    
    private List<String> extractVariousModels(Element modelElement) {
    	
		String modelsLinkUrl = modelElement.attr("abs:href");

		List<String> modelsList = new JsoupWebCrawler(modelsLinkUrl).selectOne("span.cpuname")
				.map(cpuNameSpan -> cpuNameSpan.parent().parent().nextElementSibling())
				.map(modelsTr -> modelsTr.select("td").get(0).text())
				.map(modelsText -> modelsText.replace("Model Variants: ", "").split(","))
				.map(modelsArray -> Arrays.asList(modelsArray))
				.orElseThrow(() -> new IllegalStateException("Expected cpuname span on page " + modelsLinkUrl));
		
		return modelsList;
    }
}
