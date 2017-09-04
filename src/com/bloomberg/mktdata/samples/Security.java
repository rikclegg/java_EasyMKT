package com.bloomberg.mktdata.samples;

public class Security {

	private String name;
	private Securities securities;
	
	Security(Securities securities) {
		this.securities = securities;
	}

	Security(Securities securities, String name) {
		this.securities = securities;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Securities getSecurities() {
		return this.securities;
	}
}
