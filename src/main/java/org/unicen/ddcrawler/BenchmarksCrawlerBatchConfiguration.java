package org.unicen.ddcrawler;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicen.ddcrawler.abenchmark.BenchmarkFeaturesProcessor;
import org.unicen.ddcrawler.abenchmark.BenchmarkUrl;
import org.unicen.ddcrawler.abenchmark.BenchmarkUrlsReader;
import org.unicen.ddcrawler.domain.DeviceData;

@Configuration
@EnableBatchProcessing
public class BenchmarksCrawlerBatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    
    @Autowired
	private BenchmarkUrlsReader benchmarkUrlsReader;
    
    @Autowired
    private BenchmarkFeaturesProcessor benchmarkFeaturesProcessor;
    
    private static final Set<DeviceData> deviceDatas = new HashSet<>();
    
    @Bean
    public ItemWriter<Set<DeviceData>> deviceDatalWriter() {
        return new ItemWriter<Set<DeviceData>>(){

			@Override
			public void write(List<? extends Set<DeviceData>> items) throws Exception {
			    
			    items.forEach(devicesData -> deviceDatas.addAll(devicesData));
			}};
    }
    
    @Bean
    public Step findBenchmarkFeaturesStep() {
        return stepBuilderFactory.get("findBenchmarkFeaturesStep")
                .<BenchmarkUrl, Set<DeviceData>> chunk(1)
                .reader(benchmarkUrlsReader)
                .processor(benchmarkFeaturesProcessor)
                .writer(deviceDatalWriter())
                .build();
    }

    @Bean
    public Job extractBenchmarkFeaturesJob() {
        return jobBuilderFactory.get("extractBenchmarkFeaturesJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(findBenchmarkFeaturesStep())
                .end()
                .build();
    }
    
    @Bean
    public JobExecutionListener listener() {
    	return new JobCompletionNotificationListener();
    }
        
    private static class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    
    	@Override
    	public void afterJob(JobExecution jobExecution) {
    		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
    		
    			System.out.println("!!! JOB FINISHED! Total urls: " + deviceDatas);
    		}
    	}
    }
}