
public class Main {

		DataStore dataStore;
		SearchAlgorithm searchAlgorithm;

	/* Arguments...
	 * 	[0] = name to search for,
	 *  
	 *  [1] = file to search. This should be of type '.txt'. 
	 *  The contents of the file should consist of a series of entries as ordered pairs (the name followed by the number).
	 *  These entries should be separated by a newline.
	 *  
	 *  [2] = algorithm to use for the search.
	 *   */
	public static void main(String args[]) {
		
//		if (args[1] !=) //TODO Check for bad filename input 
		SearchAlgorithm searchAlgorithm;	
		
		String argumentsInfo = "\nArguments should be as follows...\n <name> <file> <algorithm-optional>";
		
		if (args[3] != null) {
			System.out.println("Too many arguments supplied. " + argumentsInfo);
		} else if ((args[2] == null) || (args[1] == null) || (args[0] == null)) {
			System.out.println("Too few arguments supplied. " + argumentsInfo);
		}
		
		if (args[2].toLowerCase() == "binary-search") {
			searchAlgorithm = new BinarySearch();
		} else {
			System.out.println("\nUnrecognized algorithm specified. Defaulting to Binary-Chop");
			searchAlgorithm = new BinarySearch();
		}
		
		System.out.println("\nSearching for: " + args[0] + " in " + args[1] + " using " + searchAlgorithm.toString() + "...");
		
		String value = new String();
		
		DataStore = new arrayList
		
		
		
		System.out.println("\nRetrieved " + value + " for " + args[0]);
		return; //TODO Return or System.exit or just nothing?
	}

}
