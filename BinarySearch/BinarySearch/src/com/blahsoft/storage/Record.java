package com.blahsoft.storage;

/* This class encapsulates keys and their associated values in a single object. 
 * The constructor or getters could potentially be used to parse the input values for lightweight
 * storage or output formatting. */
public interface Record {

	String getKey();
	String getValue();

}
