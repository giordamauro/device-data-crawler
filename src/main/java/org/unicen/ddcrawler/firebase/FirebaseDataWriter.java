package org.unicen.ddcrawler.firebase;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FirebaseDataWriter implements ItemWriter<FirebaseData> {

	private static final Log LOGGER = LogFactory.getLog(FirebaseDataWriter.class);
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String firebaseBaseUrl;
	
	@Autowired
	public FirebaseDataWriter(@Value("${firebase.baseUrl}")String firebaseBaseUrl) {
		this.firebaseBaseUrl = firebaseBaseUrl;
	}	

	@Override
	public void write(List<? extends FirebaseData> firebaseDataItems) throws Exception {
		
		firebaseDataItems.forEach(firebaseData -> {
			
			String url = firebaseBaseUrl + firebaseData.getUrlId() + ".json";
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Object> request = new HttpEntity<>(firebaseData.getData() ,headers);
			
			try {
				restTemplate.put(url, request);
				
				LOGGER.info("Successfully stored " + firebaseData);
			}
			catch(Exception e){
				
				LOGGER.error("Exception storing " + firebaseData, e);
			}
		});
	}
}
