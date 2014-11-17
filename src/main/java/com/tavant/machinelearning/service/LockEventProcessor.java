package com.tavant.machinelearning.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tavant.machinelearning.model.Event;
import com.tavant.machinelearning.notifications.ApnsCommunication;

public class LockEventProcessor implements EventProcessor{
	private static final Logger logger = LoggerFactory.getLogger(LockEventProcessor.class);
	
	public void processEvent(Event event) {
		/* First save the data to DB and then check for usual/unusual event */
		
		//Predict only if the lock is unlocked
		if(event.getStatus().equalsIgnoreCase("unlocked")) {
			LockEventDetector detector = new LockEventDetector();
			Boolean isEvenUnUsual = detector.isEventUnusual(event.getUpdated());
			logger.info("Event => isEvenUnUsual: " + isEvenUnUsual);
			
			if(isEvenUnUsual) {
				//67945533470607d89c6319d88b34f5ed0ddf767a45528234f54f1f40194fcf8c
				//7eee7b8153d213468e6ec3ea6abb2638f140f764
				//73d110de1034a6394327a5ea5082b2739f909176b0a20646a5f648c3fbb9f155
		
				ApnsCommunication  mApnsCommunication    =  new ApnsCommunication("73d110de1034a6394327a5ea5082b2739f909176b0a20646a5f648c3fbb9f155", 1, "Your front door is unlocked, at unusual time, please check!",5);
			}
		}else {
			logger.info("Event => isEvenUnUsual: false");
		}
		
	}
	
	private Map<Integer, Integer> getFrequencyPerHour() {

		String csvFile = "LockRaw.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Map<Integer, Integer> frequencyPerHour = new HashMap<Integer, Integer>();

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] event = line.split(cvsSplitBy);

				System.out.println("Event = " + event[3] + " , time ="
						+ event[4]);
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date formattedDate = formatter.parse(event[4]);
				Calendar cal = Calendar.getInstance();
				cal.setTime(formattedDate);
				int hourOfTheDay = cal.get(Calendar.HOUR_OF_DAY);
				
				if(frequencyPerHour.containsKey(hourOfTheDay)) {
					int value  = frequencyPerHour.get(hourOfTheDay);
					frequencyPerHour.put(hourOfTheDay, value + 1);
				} else {
					frequencyPerHour.put(hourOfTheDay,  1);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println(frequencyPerHour);
		return frequencyPerHour;
	}
	
	public static void main(String[] args) {
		LockEventProcessor processor = new LockEventProcessor();
		processor.getFrequencyPerHour();
	}
}
