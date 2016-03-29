package org.unicen.ddcrawler;


import java.net.MalformedURLException;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.firebase.FirebaseData;
import org.unicen.ddcrawler.firebase.FirebaseDataWriter;
import org.unicen.ddcrawler.firebase.ModelDeviceFirebaseProcessor;
import org.unicen.ddcrawler.writer.DeviceModelRepositoryReader;

@EnableBatchProcessing
@Configuration
public class FirebaseBatchConfiguration {
	
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    

    @Autowired
    private DeviceModelRepositoryReader deviceModelRepositoryReader;
    
    @Autowired
	private ModelDeviceFirebaseProcessor modelDeviceFirebaseProcessor;
    
    @Autowired
	private FirebaseDataWriter firebaseDataWriter;
    
    @Bean
    public Step exportToFirebaseStep() throws MalformedURLException {
        return stepBuilderFactory.get("exportToFirebaseStep")
                .<DeviceModel, FirebaseData> chunk(1)
                .reader(deviceModelRepositoryReader)
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