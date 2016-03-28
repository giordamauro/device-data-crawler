package org.unicen.ddcrawler;


import java.net.MalformedURLException;
import java.util.Collections;

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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.firebase.FirebaseData;
import org.unicen.ddcrawler.firebase.FirebaseDataWriter;
import org.unicen.ddcrawler.firebase.ModelDeviceFirebaseProcessor;
import org.unicen.ddcrawler.repository.DeviceModelRepository;

@EnableBatchProcessing
@Configuration
public class FirebaseBatchConfiguration {
	
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    

    @Autowired
    private DeviceModelRepository deviceModelRepository;
    
    @Autowired
	private ModelDeviceFirebaseProcessor modelDeviceFirebaseProcessor;
    
    @Autowired
	private FirebaseDataWriter firebaseDataWriter;

	@Bean
	public RepositoryItemReader<DeviceModel> reader() {
		RepositoryItemReader<DeviceModel> reader = new RepositoryItemReader<>();
		reader.setRepository(deviceModelRepository);
		reader.setMethodName("findAll");
		reader.setPageSize(5);
		reader.setSort(Collections.singletonMap("featuresCount", Direction.DESC));
		
		return reader;
	}    
    
    @Bean
    public Step exportToFirebaseStep() throws MalformedURLException {
        return stepBuilderFactory.get("exportToFirebaseStep")
                .<DeviceModel, FirebaseData> chunk(5)
                .reader(reader())
                .processor(modelDeviceFirebaseProcessor)
                .writer(firebaseDataWriter)
                .build();
    }

    
    @Bean
    public Job exportToFirebaseJob() throws MalformedURLException {
        return jobBuilderFactory.get("exportToFirebase")
                .incrementer(new RunIdIncrementer())
                .listener(new JobCompletionNotificationListener())
                .flow(exportToFirebaseStep())
                .end()
                .build();
    }
    
    @Bean
    public JobExecutionListener listener() {
    	return new JobCompletionNotificationListener();
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