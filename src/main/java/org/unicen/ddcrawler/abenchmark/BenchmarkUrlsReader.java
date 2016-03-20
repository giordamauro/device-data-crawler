package org.unicen.ddcrawler.abenchmark;

import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class BenchmarkUrlsReader implements ItemReader<BenchmarkUrl> {

    private Iterator<Entry<String, String>> urlIterator;

    @Autowired
	public BenchmarkUrlsReader(BenchmarkProperties benchmarkProperties) {
		
        this.urlIterator = benchmarkProperties.getUrls().entrySet().iterator();
	}

	@Override
	public BenchmarkUrl read() throws Exception {
		
		if(urlIterator.hasNext()){
		    
		    Entry<String, String> benchmarkUrl = urlIterator.next();
		    
			return new BenchmarkUrl(benchmarkUrl.getKey(), benchmarkUrl.getValue());
		}

		return null;
	}
}
