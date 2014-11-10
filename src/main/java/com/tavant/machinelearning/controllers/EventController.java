package com.tavant.machinelearning.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tavant.machinelearning.model.Event;
import com.tavant.machinelearning.service.AssociationEventProcessor;
import com.tavant.machinelearning.service.AutoRuleProcessor;
import com.tavant.machinelearning.service.LockEventProcessor;

/*
 * Rest Service to consume events
 * Events are processed and saved to DB
 * 
 * For each event, we also detect any associated event and predict whether the event is Usual/Unusual
 */
@RestController
public class EventController {
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);

	@RequestMapping(value = "/event", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String event(@RequestBody Event event) {
		logger.info("###################################################");
		logger.info("Event => deviceType: " + event.getWhatami());
		logger.info("Event => deviceName: " + event.getName());
		logger.info("Event => timestamp: " + event.getUpdated());
		logger.info("###################################################");
		String device = event.getWhatami();
		String deviceType =  device.split("/")[device.split("/").length-1];
		
		if(event.getName().equalsIgnoreCase("Lux Lamp")) { //deviceType.equalsIgnoreCase("lock")
			LockEventProcessor lockEventProcessor = new LockEventProcessor();
			lockEventProcessor.processEvent(event);
		}
		
		if(event.getName().equalsIgnoreCase("Lux Lamp 1")) {
			AssociationEventProcessor processor = new AssociationEventProcessor();
			processor.processEvent(event);
		}
		
		if(event.getName().equalsIgnoreCase("Gate") && event.getStatus().equalsIgnoreCase("UnLocked")) {
			AutoRuleProcessor processor = new AutoRuleProcessor();
			processor.processEvent(event);
		}
		
		return "Success";
	}
}
