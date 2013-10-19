package com.blahsoft.storage;

import java.util.ArrayList;


/* This class returns the value from a dataStore in the form of a ChoppableArrayList of Records for a record with a specified key. */
//N.B. This search ignores case differences
public class BinarySearchAlgorithm {

	public static String pullKeyValue(String key, ArrayList<Record> dataStore) {
		
		if (dataStore.isEmpty()) {
			return null;
		}

		int pivotIndex = (dataStore.size())/2;
		
		Record pivotRecord = dataStore.get(pivotIndex);
		String pivotKey = pivotRecord.getKey();

		if (pivotKey.equalsIgnoreCase(key)) {
			return pivotRecord.getValue();
		} else if (pivotKey.compareToIgnoreCase(key) < 0 ) { //i.e. is the key greater than the pivotKey
			dataStore.subList(0, pivotIndex+1).clear();
			return pullKeyValue(key, dataStore);
		} else if (pivotKey.compareToIgnoreCase(key) > 0 ) {//i.e. is the key less than the pivotKey
			dataStore.subList(pivotIndex, dataStore.size()).clear();
			return pullKeyValue(key, dataStore);
		}
		return null;
	}
}
