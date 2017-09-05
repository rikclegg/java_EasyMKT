package com.bloomberg.mktdata.samples;

import java.util.ArrayList;

import com.bloomberg.mktdata.samples.Notification.NotificationCategory;
import com.bloomberg.mktdata.samples.Notification.NotificationType;

public class Field {
	
	private String name;
	private String old_value;
	private String current_value;
	private Fields fields;
	
	ArrayList<NotificationHandler> notificationHandlers = new ArrayList<NotificationHandler>();

	Field(Fields fields) {
		this.fields = fields;
	}
	
	Field(Fields fields, String name, String value) {
		this.fields = fields;
		this.name = name;
		this.old_value = null;
		this.current_value = value;
	}
	
	public String name() {
		return name;
	}
	
	public String value() {
		return this.current_value;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void setCurrentValue(String value) {
		this.current_value = value;
	}
	
	void CurrentToOld() {
		this.old_value = this.current_value;
	}

	FieldChange getFieldChanged() {
		
		FieldChange fc=null;
		
		if(!this.current_value.equals(this.old_value)) {
			fc = new FieldChange();
			fc.field = this;
			fc.oldValue = this.old_value;
			fc.newValue = this.current_value;
		}
		return fc;
	}
	
	public void addNotificationHandler(NotificationHandler notificationHandler) {
		notificationHandlers.add(notificationHandler);
	}
	
	void sendNotifications(ArrayList<FieldChange> fcl) {
		if(this.notificationHandlers.size()>0) {
			Notification n = new Notification(NotificationCategory.MKTDATA, NotificationType.FIELD, this.fields.security, fcl);
			for(NotificationHandler nh: notificationHandlers) {
				if(!n.consume) nh.processNotification(n);
			}
		}
	}
}
