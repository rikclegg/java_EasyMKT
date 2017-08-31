package com.bloomberg.mktdata.samples;

import java.util.ArrayList;
import java.util.Iterator;

public class SubscriptionFields implements Iterable<SubscriptionField> {

	EasyMKT easyMKT;
	private ArrayList<SubscriptionField> subscriptionFields = new ArrayList<SubscriptionField>();

	SubscriptionFields(EasyMKT easyMKT) {
		this.easyMKT = easyMKT;
		updateSubscriptions();
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
		SubscriptionField newField = new SubscriptionField(this,fieldName);
		subscriptionFields.add(newField);
		return newField;
	}

}
