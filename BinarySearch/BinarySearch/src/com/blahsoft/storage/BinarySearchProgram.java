package com.blahsoft.storage;

import java.util.ArrayList;

/* This class reads in a file of ANSI or UTF-8 encoding that contains a sorted list of comma-separated pairs of keys and values, 
 * with each pair separated by a new line. It accepts a file-path and key as input, then returns the value within the file associated 
 * with that key */
public class BinarySearchProgram {

	/* Arguments: [0] = name to search for, [1] = the path of the file to search.*/
	public static void main(String args[]) {
		
		if (args.length != 2) {
			System.err.println("\nYou have entered an invalid number of arguments." +
								"\n\nPlease use the name to search for followed by file to search through.");
			System.exit(1);
		}
		
		String key = args[0];
		String pathName = args[1];
		
		System.out.println("\nSearching for '" + key + "' in " + pathName + "...");
		
		ArrayList<Record> dataStore = FileParser.Parse(pathName);
		
		String phoneNumber = BinarySearchAlgorithm.pullKeyValue(key, (ArrayList<Record>)dataStore);
		if (phoneNumber==null) {
			System.out.println("\nNo record was found for " + key + ".");
		} else {
			System.out.println("\nRetrieved " + phoneNumber + " for " + key + ".\n");
		}
	}	
}
