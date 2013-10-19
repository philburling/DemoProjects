package com.blahsoft.storage;

/* This class simply stores and returns key-value pairs as they have been provided to the constructor */
public class SimpleRecord implements Record {

	private String key;
	private String value;
	
	SimpleRecord(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
}
