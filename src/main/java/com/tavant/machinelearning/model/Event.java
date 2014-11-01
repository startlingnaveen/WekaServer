package com.tavant.machinelearning.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tavant.machinelearning.serializer.CustomDateDeSerializer;
import com.tavant.machinelearning.serializer.CustomDateSerializer;

public class Event {

	private String whatami;
	private String whoami;
	private String name;
	private String status;
	private Date timeStamp;
	private String dayIdentifier;
	
	public String getWhatami() {
		return whatami;
	}
	public void setWhatami(String whatami) {
		this.whatami = whatami;
	}
	public String getWhoami() {
		return whoami;
	}
	public void setWhoami(String whoami) {
		this.whoami = whoami;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeSerializer.class)
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getDayIdentifier() {
		return dayIdentifier;
	}
	public void setDayIdentifier(String dayIdentifier) {
		this.dayIdentifier = dayIdentifier;
	}
	
	
	
}
