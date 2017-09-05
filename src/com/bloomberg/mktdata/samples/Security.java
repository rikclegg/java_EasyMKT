package com.bloomberg.mktdata.samples;

import java.util.ArrayList;

import com.bloomberg.mktdata.samples.Notification.NotificationCategory;
import com.bloomberg.mktdata.samples.Notification.NotificationType;
import com.bloomberglp.blpapi.Message;

public class Security implements MessageHandler{

	private String name;
	private Securities securities;
	private Fields fields;
	ArrayList<NotificationHandler> notificationHandlers = new ArrayList<NotificationHandler>();

	Security(Securities securities) {
		this.securities = securities;
		this.fields = new Fields(this);
	}

	Security(Securities securities, String name) {
		this.securities = securities;
		this.name = name;
		this.fields = new Fields(this);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Securities getSecurities() {
		return this.securities;
	}

	public Field field(String fieldName) {
		return fields.field(fieldName);
	}
	
	@Override
	public void handleMessage(Message message) {
		this.fields.populateFields(message);
		if(fields.fieldChanges.size() > 0) this.sendNotifications(fields.fieldChanges);
	}
	
	public void addNotificationHandler(NotificationHandler notificationHandler) {
		notificationHandlers.add(notificationHandler);
	}

	void sendNotifications(ArrayList<FieldChange> fcl) {

		if(this.notificationHandlers.size()>0) {
			Notification n = new Notification(NotificationCategory.MKTDATA, NotificationType.UPDATE, this.fields.security, fcl);
			for(NotificationHandler nh: notificationHandlers) {
				if(!n.consume) nh.processNotification(n);
			}
			if(!n.consume) securities.processNotification(n);
		}
	}

}
