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
import org.unicen.ddcrawler.dspecifications.DSpecificationsUrlReader;

//@Configuration
//@EnableBatchProcessing
public class UrlCrawlerBatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    
    @Autowired
	private DSpecificationsUrlReader specificationsUrlReader;
    
    private static final Set<String> deviceModelUrls = new HashSet<>();
    
    @Bean
    public ItemWriter<String> modelUrlWriter() {
        return new ItemWriter<String>(){

			@Override
			public void write(List<? extends String> items) throws Exception {
				deviceModelUrls.addAll(items);
			}};
    }
    
    @Bean
    public Step findModelUrlsStep() {
        return stepBuilderFactory.get("findModelUrlStep")
                .<String, String> chunk(10)
                .reader(specificationsUrlReader)
                .writer(modelUrlWriter())
                .build();
    }

    @Bean
    public Job extractDeviceUrlJob() {
        return jobBuilderFactory.get("extractDeviceUrl")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(findModelUrlsStep())
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
    		
    			System.out.println("!!! JOB FINISHED! Total urls: " + deviceModelUrls.size());
    		}
    	}
    }
}