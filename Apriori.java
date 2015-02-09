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
/*
	Construct a HashTrie with keys as item in the candidates
	add method: transpose a candidate into the trie
	contains method: add the candidate to the new itemset of higher frequency
					if the candidate is a subset of some transaction
*/
class HashTrie {
    private HashMap<Integer, HashMap> root;
 
    public HashTrie() {
       root = new HashMap<Integer, HashMap>();
    }
    public HashTrie(LinkedList<int[]> candidates) {
        root = new HashMap<Integer, HashMap>();
        for (int i = 0; i < candidates.size(); i++) {
            add(candidates.get(i));
        }
    }
 
    public void add(int[] input) {
        HashMap<Integer, HashMap> node = root;
        for (int i = 0; i < input.length; i++) {
            if (node.containsKey(input[i]))
                node = node.get(input[i]);
            else {
                node.put(input[i], new HashMap<Integer, HashMap>());
                node = node.get(input[i]);
            }
        }
        node.put(-1, new HashMap<Integer, HashMap>(null)); //-1 indicating end of candidate because all item are positive
    }
 
   public void contains(int[] trans, int candSize, LinkedList<int[]> newItemset) {
	        HashMap<Integer, HashMap> currentNode = root;
	        for (int i = 0; i < candSize; i++) {
	            if (currentNode.containsKey(trans[i]))
	                currentNode = currentNode.get(input[i]);
	            else 
	                return;
	        }
	        if(currentNode.containsKey(-1))    // reach end of candidate
	        	newItemset.add(Arrays.copyOf(trans, candSize));             
	}
}
