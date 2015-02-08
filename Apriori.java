import java.io.*;
import java.util.*;

public class Apriori {
	
    private List<int[]> itemsets; //the list of current itemsets
    private String transaFile; //name of the transaction file
    private int numItems; //number of different items in the dataset
    private int numTransactions; // total number of transactions in the file
    private double minSup; // minimum support
    
    private void configure(String[] args) throws Exception
    {        
        if (args.length != 0) {
        	transaFile = args[0];
        }
        else 
        	transaFile = "data.dat";
    	
    	if (args.length >= 2) {
    		minSup = Double.parseDouble(args[1]);
    	} 	
    	else 
    		minSup = .7;
    	if (minSup > 1 || minSup < 0) throw new Exception("minSup: bad value");
    	

    	numItems = 0;
    	numTransactions=0;
    	BufferedReader input = new BufferedReader(new FileReader(transaFile));

    	while (input.ready()) {    		
    		String line=input.readLine();
    		numTransactions++;
    		StringTokenizer t = new StringTokenizer(line," ");
    		while (t.hasMoreTokens()) {
    			int x = Integer.parseInt(t.nextToken());
    			if (x + 1 > numItems) {
    				numItems = x + 1;
    			}
    		}    		
    	}  

    }
}
