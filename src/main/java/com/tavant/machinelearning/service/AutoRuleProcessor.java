package com.tavant.machinelearning.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tavant.machinelearning.model.Event;
import com.tavant.machinelearning.model.Rule;

public class AutoRuleProcessor implements EventProcessor{

	private static final Logger logger = LoggerFactory.getLogger(AutoRuleProcessor.class);
	@Override
	public void processEvent(Event event) {
		/* First save the data to DB and then perform automation rule */
		AutoRuleDetector detector = new AutoRuleDetector();
		Rule rule = detector.returnRule(event);
		
		if(!rule.getTargetEvent().equalsIgnoreCase(event.getStatus())) {
			//Check if the gate should be locked
			logger.info("Automation Rule Foud !!!" );
			try {
				sendPost();	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
