package com.tavant.machinelearning.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tavant.machinelearning.model.Event;
import com.tavant.machinelearning.model.Rule;

public class AssociationEventProcessor implements EventProcessor {

	private static final Logger logger = LoggerFactory.getLogger(AssociationEventProcessor.class);
	
	@Override
	public void processEvent(Event event) {
		
		/*
		 * Save the event to DB and process the event for associations
		 */
		AssociationDetector detector = new AssociationDetector();
		try {
			Rule rule = detector.getAssociationRule(event);
			logger.info("###################################################");
			logger.info("Source device: " + rule.getSourceDeviceName());
			logger.info("Source status: " + rule.getSourceEvent());
			logger.info("Targe device: " +  rule.getTargetDeviceName());
			logger.info("Target status: " + rule.getTargetEvent());
			logger.info("Event name: " + event.getName());
			logger.info("###################################################");
			
			if(event.getName().equalsIgnoreCase("Lux Lamp 1") && event.getStatus().equalsIgnoreCase("on")) {
				logger.info("Foud rule!!!" );
		        
		        sendPost();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendPost() throws Exception {
		 
		String url = "http://192.168.2.10:7798/perform/on/Lux%20Lamp";
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
	 
		// add header
		//post.setHeader("User-Agent", USER_AGENT);
	 
	 
		HttpResponse response = client.execute(post);
		logger.info("Response Code : " 
	                + response.getStatusLine().getStatusCode());
 
	}

}
