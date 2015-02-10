import java.io.*;
import java.util.*;

public class Apriori {

    private LinkedList<int[]> itemsets; //the list of current itemsets
    private String transaFile; //name of the transaction file
    private int numItems; //max number of items in a transaction
    private int numTransactions; // total number of transactions in the file
    private double minSup; // minimum support
    private LinkedList<int[]> candidates;
    private LinkedList<int[]> transactions;

    private Apriori() {
        itemsets = new LinkedList<int[]>();
        candidates = new LinkedList<int[]>();
        transactions = new LinkedList<int[]>();
    }
    
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
    		String line = input.readLine();
    		numTransactions++;
    		StringTokenizer t = new StringTokenizer(line," ");
            int lengthSoFar = 0;
    		while (t.hasMoreTokens()) {
    			int x = Integer.parseInt(t.nextToken());
                int[] temp = new int[1];
                temp[0] = x;
                itemsets.add(temp);
                lengthSoFar++;
    			if (lengthSoFar > numItems) {
    				numItems = lengthSoFar;
    			}
    		}
            int[] lineBreak = new int[1];
            lineBreak[0] = -1;
            itemsets.add(lineBreak);
    	}  
        int start = 0;
        for(int i = 0; i < numTransactions; i++) {
            int[] transaction = new int[numItems];
            for (int j = start; j < itemsets.size(); j++) {
                int[] temp = itemsets.get(j);
                if(temp[0] == -1) {
                    itemsets.remove(j);
                    start = j;
                    break;
                }
                transaction[j-start] = temp[0];
                System.out.println(j-start);
            }
            transactions.add(transaction);
        }
    }

    public static void main(String[] args)throws Exception {
    	Apriori test = new Apriori();
        test.configure(args);
        for(int i = 0; i < test.transactions.size(); i++) {
            System.out.println(Arrays.toString(test.transactions.get(i)));
            System.out.println();
            System.out.println("numItems = " + test.numItems);
            System.out.println("itemsets.size = " + test.itemsets.size());
            System.out.println();
        }
    }
}

class HashTrie {
    private HashMap<int[], Integer> root;
 
    public HashTrie() {
       root = new HashMap<int[], Integer>();
    }
    public HashTrie(LinkedList<int[]> candidates) {
        root = new HashMap<int[], Integer>();
        for (int i = 0; i < candidates.size(); i++) {
            add(candidates.get(i));
        }
    }
 
    public void add(int[] input) {
        HashMap<int[], Integer> node = root;
        Set<int[]> keySet = node.keySet();
        boolean found = false;
        for (int[] s: keySet) {
            if (Arrays.equals(input, s)) {
                node.put(input, node.get(input)+1);
                found = true;
                break;
            }
        }
        if(!found) {
            node.put(input, 1);
        } 
    }
 
    public LinkedList<int[]> generateNewItemsets(LinkedList<int[]> transactions) {
        HashMap<int[], Integer> currentNode = root;
        LinkedList<int[]> newItemsets = null;
        boolean safe = false;
        for (int[] key : currentNode.keySet()) {
            for (int i = 0; i < transactions.size(); i++) {
                if(Arrays.equals(key, Arrays.copyOf(transactions.get(i), key.length))) {
                    safe = true;
                    break;
                }
            }
            if(!safe) {
                currentNode.remove(key);
            }
        }
        newItemsets.addAll(currentNode.keySet());
        return newItemsets;  
    }
}
