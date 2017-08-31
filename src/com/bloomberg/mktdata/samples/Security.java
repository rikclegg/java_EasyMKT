package com.bloomberg.mktdata.samples;

public class Security {

	private String name;
	private SubscriptionFields subscriptionFields;
	
	Security(SubscriptionFields subscriptionFields) {
		this.subscriptionFields = subscriptionFields;
	}

	Security(SubscriptionFields subscriptionFields, String name) {
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
