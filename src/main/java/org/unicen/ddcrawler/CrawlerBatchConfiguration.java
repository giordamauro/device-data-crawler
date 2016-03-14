package org.unicen.ddcrawler;


import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicen.ddcrawler.domain.DeviceData;
import org.unicen.ddcrawler.dspecifications.DSpecificationsProcessor;
import org.unicen.ddcrawler.dspecifications.DSpecificationsUrlReader;

@Configuration
@EnableBatchProcessing
public class CrawlerBatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    
    @Autowired
	private DSpecificationsUrlReader specificationsUrlReader;
    
    @Autowired
	private DSpecificationsProcessor specificationsDataProcessor;

    @Autowired
	private JpaDeviceDataRepository jpaDeviceDataRepository;

    
    @Bean
    public Step storeModelSpecificationsStep() {
        return stepBuilderFactory.get("storeModelSpecificationsStep")
                .<String, DeviceData> chunk(5)
                .reader(specificationsUrlReader)
                .processor(specificationsDataProcessor)
                .writer(jpaDeviceDataRepository)
                .faultTolerant()
                .listener(logSkipListener())
                .build();
    }

    @Bean
    public Job extractDeviceDataJob() {
        return jobBuilderFactory.get("extractDeviceData")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(storeModelSpecificationsStep())
                .end()
                .build();
    }
    
    @Bean
    public JobExecutionListener listener() {
    	return new JobCompletionNotificationListener();
    }
    
    @Bean
    public StepExecutionListener logSkipListener() {
    	return new StepExecutionListener(){

			@Override
			public void beforeStep(StepExecution stepExecution) {
			}

			@Override
			public ExitStatus afterStep(StepExecution stepExecution) {

				if(stepExecution.getExitStatus() == ExitStatus.FAILED){
					System.out.println(stepExecution.getFailureExceptions());
				}
				
				return ExitStatus.EXECUTING;
			}};
    }
    
    private static class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    
    	@Override
    	public void afterJob(JobExecution jobExecution) {
    		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
    		
    			System.out.println("!!! JOB FINISHED!");
    		}
    	}
    }
}