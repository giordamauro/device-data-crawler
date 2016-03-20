package org.unicen.ddcrawler;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.unicen.ddcrawler.domain.DeviceDataUrl;
import org.unicen.ddcrawler.dspecifications.DSpecificationsWebUrlReader;
import org.unicen.ddcrawler.writer.JpaDeviceUrlRepository;


//@Configuration
//@EnableBatchProcessing
public class DSUrlsCrawlerBatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    
    @Autowired
	private DSpecificationsWebUrlReader specificationsUrlReader;
    
    @Autowired
    private JpaDeviceUrlRepository jpaDeviceDataRepository;
    
    @Bean
    public Step findModelUrlsStep() {
        return stepBuilderFactory.get("findModelUrlStep")
                .<String, DeviceDataUrl> chunk(100)
                .reader(specificationsUrlReader)
                .processor(urlProcessor())
                .writer(jpaDeviceDataRepository)
                .build();
    }

    @Bean
    public Job extractDeviceUrlJob() {
        return jobBuilderFactory.get("extractDeviceUrl")
                .incrementer(new RunIdIncrementer())
                .flow(findModelUrlsStep())
                .end()
                .build();
    }
    
    @Bean
    public ItemProcessor<String, DeviceDataUrl> urlProcessor() {

    	return new ItemProcessor<String, DeviceDataUrl>(){

    		@Override
			public DeviceDataUrl process(String item) throws Exception {
				return new DeviceDataUrl(item);
			}
    	};
    }        

}