package com.yf.base.actions.location;


import com.opensymphony.xwork2.ActionSupport;

public class WarnEventShow extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String eventId;
	private String eventType;
	private String personId;
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getPersonId() {
		return personId;
	}
	
	public void setPersonId(String personId) {
		this.personId = personId;
	}

}
