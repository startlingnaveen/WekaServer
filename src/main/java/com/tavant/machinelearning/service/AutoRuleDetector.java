package com.tavant.machinelearning.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tavant.machinelearning.model.Event;
import com.tavant.machinelearning.model.Rule;

public class AutoRuleDetector {
	HashMap<Integer, HashMap<Integer, Integer>> mHashMapWeekend = new HashMap<Integer, HashMap<Integer, Integer>>();
	HashMap<Integer, HashMap<Integer, Integer>> mHashMapWeekday = new HashMap<Integer, HashMap<Integer, Integer>>();
	String[] deviceStates = {"locked","unlocked"};
//	public static void main(String[] args) {
//		boolean a = true;
//		Executor obj = new Executor();
//		obj.run();
////		while(a){
////			System.out.println("Enter the device, state and day identifier separated by spaces");
////			Scanner scan= new Scanner(System.in);
////			String text= scan.nextLine();
////		    System.out.println(obj.operateDevice("a","b","weekday").get(1));
////		}
//		Calendar cal = new GregorianCalendar(2014, 10, 31, 19, 14);
//		Event ev = new Event("GATE", "UNLOCKED", "WEEKDAY", cal);
//		System.out.println("YEs Got it..."+obj.returnRule(ev).getState());
//	  }
	
	// This method reads data from the CSV and populates the Hashmap instance variables.
	public void run() {
		String csvFile = "assets/mockBulbData.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			    // use comma as separator
				String[] fields = line.split(cvsSplitBy);
				try {
					Date sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(fields[0]);
					
					Calendar cal = new GregorianCalendar();
					cal.setTime(sdf);
					
					int hours = cal.get(Calendar.HOUR_OF_DAY);
					int minutes = roundOffVal(cal.get(Calendar.MINUTE));
					if(fields[3].trim().equalsIgnoreCase("weekday")){
						populateData(mHashMapWeekday,hours,minutes);
					}
					if(fields[3].trim().equalsIgnoreCase("weekend")){
						populateData(mHashMapWeekend,hours,minutes);
					}
						
				} catch (ParseException e) {
					e.printStackTrace();
				}
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		
		}
	
	// Helper method for run
	void populateData(HashMap<Integer, HashMap<Integer, Integer>> mHashMap, int hours, int minutes){
		if(mHashMap.containsKey(hours)){
			HashMap<Integer, Integer> currMap = mHashMap.get(hours);
			if(currMap.containsKey(minutes)){
				currMap.put(minutes, currMap.get(minutes) + 1);
			}
			else{
				currMap.put(minutes, 1);
			}
		}
		else{
			HashMap<Integer, Integer> mMap = new HashMap<Integer, Integer>();
			mMap.put(minutes, 1);
			mHashMap.put(hours, mMap);
		}
	}
	
	// Method that rounds off the minute value to the greater value of the ten minute range.
	int roundOffVal(int m){
		int x=0;
		int[] medians = {0,10,20,30,40,50,59};
		for(int i=0;i<medians.length;i++){
			if(m>=medians[i]&& m<medians[i+1])
				x=medians[i+1];
		}
		return x;
	}
	
	// Method that returns the arraylist of timings at which the device attains the specified state WRT to the dayIdentifier
	ArrayList<String> operateDevice(String device, String state,String dayIdentifier){
		ArrayList<String> a = new ArrayList<String>();
		if(dayIdentifier.equalsIgnoreCase("weekend")){
			a = returnListData(mHashMapWeekend,2);
		}
		if(dayIdentifier.equalsIgnoreCase("weekday")){
			a = returnListData(mHashMapWeekday,5);
		}
		return a;
	}
	
	// Helper method for operateDevice
	ArrayList<String> returnListData(HashMap<Integer, HashMap<Integer, Integer>> mHashMap, int idx){
		ArrayList<String> predictions = new ArrayList<String>();
		Object[] keys = mHashMap.keySet().toArray();
			for(int i=0;i<keys.length;i++){
				HashMap<Integer, Integer> intermap = mHashMap.get(keys[i]);
				Object[] interKeys = intermap.keySet().toArray();
				for(int j=0;j<interKeys.length;j++){
					if(intermap.get(interKeys[j])==idx)
						predictions.add(keys[i]+":"+interKeys[j]);
					}	
		}
			return predictions;
			
	}
	
	// Method That returns the rule for a device based on the time specified.
	Rule returnRule(Event e){
		
		Rule rule = new Rule();
		String dayIdentifier = e.getDayIdentifier();
		if(dayIdentifier.equalsIgnoreCase("WEEKDAY")){
			rule = findOutRule(e, mHashMapWeekday, 5);
		}
			
		else if(dayIdentifier.equalsIgnoreCase("WEEKEND")){
			rule = findOutRule(e, mHashMapWeekend, 2);
		}
			
		
		return rule;
	}
	
	// Helper method for returnRule
	Rule findOutRule(Event e, HashMap<Integer, HashMap<Integer, Integer>> mapToBeUsed, int frequency){
		Calendar cal = new GregorianCalendar();
		cal.setTime(e.getUpdated());
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int min =  cal.get(Calendar.MINUTE);
		int minutes = roundOffVal(min);
		Rule rule = new Rule();
		if(mapToBeUsed!=null && mapToBeUsed.containsKey(hours)){
			HashMap<Integer, Integer> currMap = mapToBeUsed.get(hours);
			if(currMap.containsKey(minutes)){
				if(currMap.get(minutes)==frequency)
					//rule = new Rule("Gate","Locked");	
					rule.setTargetDeviceName("Gate");
					rule.setTargetEvent("Locked");
			}
			else
				//rule = new Rule("Gate","Unlocked");
				rule.setTargetDeviceName("Gate");
				rule.setTargetEvent("Unlocked");
		}
		else
			//rule = new Rule("Gate","Unlocked");
			rule.setTargetDeviceName("Gate");
			rule.setTargetEvent("Unlocked");
		return rule;
	}
	
	
}
