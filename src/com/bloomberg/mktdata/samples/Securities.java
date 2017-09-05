package com.bloomberg.mktdata.samples;

import java.util.ArrayList;
import java.util.Iterator;

import com.bloomberg.mktdata.samples.Log.LogLevels;

public class Securities implements Iterable<Security>, NotificationHandler {

	EasyMKT easyMKT;
	private ArrayList<Security> securities = new ArrayList<Security>();
	ArrayList<NotificationHandler> notificationHandlers = new ArrayList<NotificationHandler>();

	Securities(EasyMKT easyMKT) {
		this.easyMKT = easyMKT;
	}
	
	@Override
	public Iterator<Security> iterator() {
		return securities.iterator();
	}

	public Security get(int index) {
		return securities.get(index);
	}
	
	public Security get(String name) {
		for(Security s: securities) {
			if(s.getName().equals(name)) return s;
		}
		return null;
	}

	Security createSecurity(String ticker) {
		Log.LogMessage(LogLevels.DETAILED, "Adding new security: " + ticker);
		Security newSecurity = new Security(this,ticker);
		securities.add(newSecurity);
		Log.LogMessage(LogLevels.DETAILED, "Added new security: " + newSecurity.getName());
		return newSecurity;
	}

	public void addNotificationHandler(NotificationHandler notificationHandler) {
		notificationHandlers.add(notificationHandler);
	}

	@Override
	public void processNotification(Notification notification) {
		for(NotificationHandler nh: notificationHandlers) {
			if(!notification.consume) nh.processNotification(notification);
		}
	}
}
