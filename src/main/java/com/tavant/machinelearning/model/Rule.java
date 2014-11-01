package com.tavant.machinelearning.model;

public class Rule {
	
	private String sourceDeviceId;
	private String sourceDeviceType;
	private String sourceDeviceName;
	private String sourceEvent;
	
	private String targetDeviceId;
	private String targetDeviceType;
	private String targetDeviceName;
	
	private String targetEvent;
	
	public String getSourceDeviceId() {
		return sourceDeviceId;
	}

	public void setSourceDeviceId(String sourceDeviceId) {
		this.sourceDeviceId = sourceDeviceId;
	}

	public String getSourceDeviceType() {
		return sourceDeviceType;
	}

	public void setSourceDeviceType(String sourceDeviceType) {
		this.sourceDeviceType = sourceDeviceType;
	}

	public String getSourceDeviceName() {
		return sourceDeviceName;
	}

	public void setSourceDeviceName(String sourceDeviceName) {
		this.sourceDeviceName = sourceDeviceName;
	}

	public String getTargetDeviceId() {
		return targetDeviceId;
	}

	public void setTargetDeviceId(String targetDeviceId) {
		this.targetDeviceId = targetDeviceId;
	}

	public String getTargetDeviceType() {
		return targetDeviceType;
	}

	public void setTargetDeviceType(String targetDeviceType) {
		this.targetDeviceType = targetDeviceType;
	}

	public String getTargetDeviceName() {
		return targetDeviceName;
	}

	public void setTargetDeviceName(String targetDeviceName) {
		this.targetDeviceName = targetDeviceName;
	}

	public String getSourceEvent() {
		return sourceEvent;
	}

	public void setSourceEvent(String sourceEvent) {
		this.sourceEvent = sourceEvent;
	}

	public String getTargetEvent() {
		return targetEvent;
	}

	public void setTargetEvent(String targetEvent) {
		this.targetEvent = targetEvent;
	}

	
	
	
	
}
