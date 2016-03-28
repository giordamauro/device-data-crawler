package org.unicen.ddcrawler;


import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.UrlResource;
import org.unicen.ddcrawler.domain.DeviceModel;
import org.unicen.ddcrawler.gsupport.AndroidDevice;
import org.unicen.ddcrawler.gsupport.GSupportedDevicesProcessor;
import org.unicen.ddcrawler.writer.JpaDeviceDataWriter;

//@EnableBatchProcessing
//@Configuration
public class GoogleSupportedBatchConfiguration {

	@Value("${google.supportedDevices.csv}")
	private String googleSupportedDevicesCsv;
	
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    
    @Autowired
	private GSupportedDevicesProcessor gSupportedDevicesProcessor;    
    
    @Autowired
	private JpaDeviceDataWriter jpaDeviceDataRepository;

    @Bean
    public FlatFileItemReader<AndroidDevice> reader() throws MalformedURLException {
        FlatFileItemReader<AndroidDevice> reader = new FlatFileItemReader<AndroidDevice>();
        reader.setResource(new UrlResource(googleSupportedDevicesCsv));
        reader.setEncoding("UTF-16");
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<AndroidDevice>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "retailBranding", "marketingName", "device", "model" });
                setStrict(false);
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<AndroidDevice>() {{
                setTargetType(AndroidDevice.class);
            }});
        }});
        return reader;
    }
    
    
    @Bean
    public Step storeSupportedModelsStep() throws MalformedURLException {
        return stepBuilderFactory.get("storeSupportedModelsStep")
                .<AndroidDevice, DeviceModel> chunk(10)
                .reader(reader())
                .processor(gSupportedDevicesProcessor)
                .writer(jpaDeviceDataRepository)
                .faultTolerant().retry(SocketTimeoutException.class)
                .listener(new LogSkipListener())
                .build();
    }

    
    @Bean
    public Job extractDeviceDataJob() throws MalformedURLException {
        return jobBuilderFactory.get("extractDeviceData")
                .incrementer(new RunIdIncrementer())
                .listener(new JobCompletionNotificationListener())
                .flow(storeSupportedModelsStep())
                .end()
                .build();
    }
    
    @Bean
    public JobExecutionListener listener() {
    	return new JobCompletionNotificationListener();
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