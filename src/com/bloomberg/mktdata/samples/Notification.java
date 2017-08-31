package com.bloomberg.mktdata.samples;

import java.util.ArrayList;

public class Notification {
	
	public enum NotificationCategory {
		MKTDATA,
		ADMIN
	}
	
	public enum NotificationType { 
		NEW,
		UPDATE,
		ERROR
	}

	public NotificationCategory category;
	public NotificationType type;
	public boolean consume = false; 
	
	private Security security;
	
	private ArrayList<FieldChange>fieldChanges;
	private int errorCode;
	private String errorMessage;
	
	Notification (NotificationCategory category, NotificationType type, Security security, ArrayList<FieldChange> fieldChanges) {
		this.category = category;
		this.type = type;
		this.security = security;
		this.fieldChanges = fieldChanges;
	}
	
	Notification (NotificationCategory category, NotificationType type, Security security, int errorCode, String errorMessage) {
		this.category = category;
		this.type = type;
		this.security = security;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Security getSecurity() {
		return this.security;
	}
	
	public ArrayList<FieldChange> getFieldChanges() {
		return this.fieldChanges;
	}
	
	public int errorCode() {
		return this.errorCode;
	}
	
	public String errorMessage() {
		return this.errorMessage;
	}
}
