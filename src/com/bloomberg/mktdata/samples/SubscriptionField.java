package com.bloomberg.mktdata.samples;

public class SubscriptionField {

	private String name;
	private SubscriptionFields subscriptionFields;
	
	SubscriptionField(SubscriptionFields subscriptionFields) {
		this.subscriptionFields = subscriptionFields;
	}

	SubscriptionField(SubscriptionFields subscriptionFields, String name) {
		this.subscriptionFields = subscriptionFields;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public SubscriptionFields getSubscriptionFields() {
		return this.subscriptionFields;
	}
}
