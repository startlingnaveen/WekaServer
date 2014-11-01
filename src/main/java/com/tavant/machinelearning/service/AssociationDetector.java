package com.tavant.machinelearning.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;

import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.Item;
import weka.core.Instances;

import com.tavant.machinelearning.model.Event;
import com.tavant.machinelearning.model.Rule;

public class AssociationDetector {

	public static void main(String args[]) throws Exception{
		AssociationDetector detector = new AssociationDetector();
		Rule rule = detector.getAssociationRule(null);
		
		System.out.println(rule.getTargetDeviceName());
		System.out.println(rule.getTargetEvent());
	}
	
	public Rule getAssociationRule(Event event) throws Exception {
		//load data
	    Instances data = new Instances(new BufferedReader(new FileReader("/Users/Naveen/Documents/workspace-sts-3.6.1.RELEASE/WekaServer/Association.arff")));
	       //build model
	       Apriori model = new Apriori();
	       model.buildAssociations(data);
	       
	       /*
	        * Loop through the rules and pick up one rule which is best.
	        * The best rule is decided based on confidence factor
	        */ 
	       
	       for (AssociationRule rule : model.getAssociationRules().getRules()) {
	    	    System.out.println("##########################################");
				System.out.println(rule);
				System.out.println("##########################################");
				
				int totalNumberOfTransctions = rule.getTotalTransactions();
				int totalSupport = rule.getTotalSupport();
				/*System.out.println("Total Transactions: " + totalNumberOfTransctions);
				System.out.println("Total Support: " + totalSupport);
				System.out.println("Source: " + rule.getPremise());
				System.out.println("Target: " + rule.getMetricValuesForRule().toString()); */
				
				
				
				if(totalNumberOfTransctions == totalSupport) {
					Rule associatedRule = new Rule();
					
					Collection<Item> items = rule.getPremise();
					
					for (Item item : items) {
						associatedRule.setSourceDeviceName(item.toString().split("=")[0]);
						associatedRule.setSourceDeviceType(item.toString().split("=")[0]);
						associatedRule.setSourceEvent(item.toString().split("=")[1]);
						
					}
					
					Collection<Item> consequences = rule.getConsequence();
					//Item item = items.toString();
					for (Item item : consequences) {
						associatedRule.setTargetDeviceName(item.toString().split("=")[0]);
						associatedRule.setTargetDeviceType(item.toString().split("=")[0]);
						associatedRule.setTargetEvent(item.toString().split("=")[1]);
					}
					
					
					
					return associatedRule;
				}
				
			
		} 
	       
		return null;
	}
 
}
