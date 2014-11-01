package com.tavant.machinelearning.service;

import com.tavant.machinelearning.model.Event;

public interface EventProcessor {

	public void processEvent(Event event);
}
