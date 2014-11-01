package com.tavant.machinelearning.service;

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
			logger.info("###################################################");
			
			if(event.getName().equalsIgnoreCase(rule.getSourceDeviceName())) {
				logger.info("Foud rule!!!" );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
