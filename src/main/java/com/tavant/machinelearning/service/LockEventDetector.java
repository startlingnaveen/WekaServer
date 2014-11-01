package com.tavant.machinelearning.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.timeseries.WekaForecaster;
import weka.classifiers.timeseries.core.TSLagMaker.Periodicity;
import weka.core.Instances;

/**
 * Predicts unusual Unlock events based on User behavior
 * using the time series forecasting API. 
 * 
 * To compile and run the CLASSPATH will need to contain:
 *
 * weka.jar (from your weka distribution)
 * pdm-timeseriesforecasting-ce-TRUNK-SNAPSHOT.jar (from the time series package)
 * jcommon-1.0.14.jar (from the time series package lib directory)
 * jfreechart-1.0.13.jar (from the time series package lib directory)
 */
public class LockEventDetector {

	private static final Logger logger = LoggerFactory.getLogger(LockEventDetector.class);
	
	public Boolean isEventUnusual(Date timeStampUnlocked) {
		
		try {
		      // path to the Australian wine data included with the time series forecasting
		      // package
		      String pathToLockData = "/Users/Naveen/Documents/workspace-sts-3.6.1.RELEASE/WekaServer/LockEvents.arff";

		      // load the wine data
		      Instances lock = new Instances(new BufferedReader(new FileReader(pathToLockData)));

		      // new forecaster
		      WekaForecaster forecaster = new WekaForecaster();

		      // set the targets we want to forecast. This method calls
		      // setFieldsToLag() on the lag maker object for us
		      forecaster.setFieldsToForecast("frequency");

		      // default underlying classifier is SMOreg (SVM) - we'll use
		      // gaussian processes for regression instead
		      forecaster.setBaseForecaster(new GaussianProcesses());

		      forecaster.getTSLagMaker().setTimeStampField("timestamp"); // date time stamp
		      forecaster.getTSLagMaker().setMinLag(1);
		      forecaster.getTSLagMaker().setMaxLag(12); // monthly data
		      forecaster.getTSLagMaker().setPeriodicity(Periodicity.HOURLY);

		      // build the model
		      forecaster.buildForecaster(lock, System.out);

		      // prime the forecaster with enough recent historical data
		      // to cover up to the maximum lag. In our case, we could just supply
		      // the 12 most recent historical instances, as this covers our maximum
		      // lag period
		      forecaster.primeForecaster(lock);

		      // forecast for 12 units (hours) beyond the end of the
		      // training data
		      List<List<NumericPrediction>> forecast = forecaster.forecast(24, System.out);

		      // output the predictions. Outer list is over the steps; inner list is over
		      // the targets
		      /*
		      for (int i = 0; i < 23; i++) {
		        List<NumericPrediction> predsAtStep = forecast.get(i);
		        for (int j = 0; j < 1; j++) {
		          NumericPrediction predForTarget = predsAtStep.get(j);
		          System.out.print("" + predForTarget.predicted() + " ");
		        }
		        System.out.println();
		      }
		      */
		     Calendar now = Calendar.getInstance();
			 now.setTime(timeStampUnlocked);
			 int hour = now.get(Calendar.HOUR_OF_DAY);
			 List<NumericPrediction> predsAtStep = forecast.get(hour);
			 int freqnecy = 0;
	        for (int j = 0; j < 1; j++) {
	          NumericPrediction predForTarget = predsAtStep.get(j);
	          freqnecy = (int) predForTarget.predicted();
	          //System.out.print("" + predForTarget.predicted() + " ");
	          logger.info("predForTarget.predicted(): " + predForTarget.predicted());
	        }
	        if(freqnecy > 1) {
	        	logger.info("Here @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	        	return false;
	        }

		      // we can continue to use the trained forecaster for further forecasting
		      // by priming with the most recent historical data (as it becomes available).
		      // At some stage it becomes prudent to re-build the model using current
		      // historical data.

		    } catch (Exception ex) {
		    	logger.info("Exception occurred @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		    	ex.printStackTrace();
		    }
		
		return true;
		
	}
	
	public static void main(String[] args) {
		LockEventDetector detector = new LockEventDetector();
		System.out.println(detector.isEventUnusual(new Date()));
		
	}
}
