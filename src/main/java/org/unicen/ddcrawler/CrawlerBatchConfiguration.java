package org.unicen.ddcrawler;


import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicen.ddcrawler.abenchmark.BenchmarkFeaturesProcessor;
import org.unicen.ddcrawler.abenchmark.BenchmarkUrl;
import org.unicen.ddcrawler.abenchmark.BenchmarkUrlsReader;
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
    private BenchmarkUrlsReader benchmarkUrlsReader;
    
    @Autowired
    private BenchmarkFeaturesProcessor benchmarkFeaturesProcessor;
    
    
    @Autowired
	private JpaDeviceDataRepository jpaDeviceDataRepository;

    @Bean
    public Step storeModelSpecificationsStep() {
        return stepBuilderFactory.get("storeModelSpecificationsStep")
                .<String, DeviceData> chunk(5)
                .reader(specificationsUrlReader)
                .processor(specificationsDataProcessor)
                .writer(jpaDeviceDataRepository)
                .faultTolerant().retry(SocketTimeoutException.class)
                .listener(new LogSkipListener())
                .build();
    }

    @Bean
    public Step storeModelBenchmarksStep() {
        return stepBuilderFactory.get("storeModelBenchmarksStep")
                .<BenchmarkUrl, Set<DeviceData>> chunk(1)
                .reader(benchmarkUrlsReader)
                .processor(benchmarkFeaturesProcessor)
                .writer(new SetJpaDeviceDataRepository(jpaDeviceDataRepository))
                .faultTolerant().retry(SocketTimeoutException.class)
                .listener(new LogSkipListener())
                .build();
    }
    
    @Bean
    public Job extractDeviceDataJob() {
        return jobBuilderFactory.get("extractDeviceData")
                .incrementer(new RunIdIncrementer())
                .listener(new JobCompletionNotificationListener())
                .flow(storeModelSpecificationsStep())
//                .next(storeModelBenchmarksStep())
                .end()
                .build();
    }
    
    @Bean
    public JobExecutionListener listener() {
    	return new JobCompletionNotificationListener();
    }
    
    public static class SetJpaDeviceDataRepository implements ItemWriter<Set<DeviceData>> {

        private final JpaDeviceDataRepository jpaDeviceDataRepository;

        public SetJpaDeviceDataRepository(JpaDeviceDataRepository jpaDeviceDataRepository) {
            this.jpaDeviceDataRepository = jpaDeviceDataRepository;
        }

        @Override
        public void write(List<? extends Set<DeviceData>> items) throws Exception {
           
            for(Set<DeviceData> deviceDataSet : items) {
                
                List<? extends DeviceData> deviceDataItems = new ArrayList<>(deviceDataSet);
                jpaDeviceDataRepository.write(deviceDataItems);
            };
        }        
    }
        
    public static class LogSkipListener extends StepExecutionListenerSupport {
        
        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {

            if(stepExecution.getExitStatus() == ExitStatus.FAILED){
                System.out.println(stepExecution.getFailureExceptions());
            }
            
            return ExitStatus.EXECUTING;
        }
    }
    
    public static class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    
        @Override
    	public void afterJob(JobExecution jobExecution) {
    		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
    		
    			System.out.println("!!! JOB FINISHED!");
    		}
    	}
    }
}