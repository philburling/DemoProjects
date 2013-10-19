package com.blahsoft.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*This class reads in a text-file and converts entries in the file into Record objects which store the data as 
 * pairs of keys and values. It then passes back them in a ChoppableArrayList collection */
public class FileParser {

	/* N.B This class method makes assumptions about the encoding of the source. 
	 * Instead 'InputStreamReader(InputStream in, String charsetName)' could be used.
	 *  or at least files with the wrong encoding could fail gracefully. It has been 
	 *  tested with ANSI and UTF-8 successfully, but cannot accept Unicode */
	public static ArrayList<Record> Parse(String filePath) {
        
		ArrayList<Record> recordList = new ArrayList<Record>(1);
        BufferedReader inputStream = null;
		
		try {
            inputStream = new BufferedReader(new FileReader(filePath));
            
            String line;
            while ((line = inputStream.readLine()) != null) {
            	String[] data = line.split(",");
            	String name = data[0];
            	String number = data[1];
            	Record newRecord = new SimpleRecord(name, number);
                recordList.add(newRecord);
            }
        } catch(IOException e) {
        	System.out.println("\nError reading from file. The filename or path may be invalid.");
        } finally {
            if (inputStream != null) {
                try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println("Error closing file.");
					e.printStackTrace();
				}
            }
        }
		//Remove null values! -Needed for when there are fewer entries than the default number pre-generated ArrayList items.
		recordList.trimToSize();  
		return recordList; 
	}
}