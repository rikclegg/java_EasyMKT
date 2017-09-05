package com.bloomberg.mktdata.samples;

import java.util.ArrayList;
import java.util.Iterator;

import com.bloomberg.mktdata.samples.Log.LogLevels;

public class SubscriptionFields implements Iterable<SubscriptionField> {

	EasyMKT easyMKT;
	private ArrayList<SubscriptionField> subscriptionFields = new ArrayList<SubscriptionField>();

	SubscriptionFields(EasyMKT easyMKT) {
		this.easyMKT = easyMKT;
	}
	
	private void updateSubscriptions() {
		// When a field is added, we cancel the existing subscriptions and re-subscribe to add the new field.
		// Ideally, there should be no subscriptions until all the fields have been added.
	}

	@Override
	public Iterator<SubscriptionField> iterator() {
		return subscriptionFields.iterator();
	}

	public SubscriptionField get(int index) {
		return subscriptionFields.get(index);
	}
	
	public SubscriptionField get(String name) {
		for(SubscriptionField sf: subscriptionFields) {
			if(sf.getName().equals(name)) return sf;
		}
		return null;
	}

	SubscriptionField createSubscriptionField(String fieldName) {
		Log.LogMessage(LogLevels.DETAILED, "Adding new subscription field: " + fieldName);
		SubscriptionField newField = new SubscriptionField(this,fieldName);
		subscriptionFields.add(newField);
		updateSubscriptions();
		Log.LogMessage(LogLevels.DETAILED, "Added new subscription field: " + newField.getName());
		return newField;
	}

	public String getFieldList() {
		
		String list="";
		
		for(SubscriptionField f: subscriptionFields) {
			list = list + f.getName() + ",";
		}
		
		if(list.length()>0) list = list.substring(0, list.length()-1);
		return list;
	}

}
