import java.io.*;
import java.util.*;

public class Apriori {

    private ArrayList<int[]> itemsets; //the list of current itemsets
    private String transaFile; //name of the transaction file
    private int numTransactions; // total number of transactions in the file
    private double minSup; // minimum support
    private ArrayList<int[]> candidates; //arraylist of integer arrays 
    private ArrayList<ArrayList<Integer>> transactions; 

    private Apriori() {
        itemsets = new ArrayList<int[]>();
        candidates = new ArrayList<int[]>();
        transactions = new ArrayList<ArrayList<Integer>>();
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

        numTransactions=0;
        BufferedReader input = new BufferedReader(new FileReader(transaFile));

        while (input.ready()) {         
            String line = input.readLine();
            numTransactions++;
            StringTokenizer t = new StringTokenizer(line," ");
            transactions.add(new ArrayList<Integer>());
            while (t.hasMoreTokens()) {
                int x = Integer.parseInt(t.nextToken());
                transactions.get(numTransactions-1).add(x);
                int[] temp = new int[1];
                temp[0] = x;
                itemsets.add(temp);
            }
        }  
    }

    public static void main(String[] args)throws Exception {
        Apriori test = new Apriori();
        test.configure(args);
        for(int i = 0; i < test.transactions.size(); i++) {
            for(int k = 0; k < test.transactions.get(i).size();k++) {
                System.out.print(test.transactions.get(i).get(k));
                System.out.print(" ");
            }
            System.out.println();
        }

        for(int i = 0; i < test.itemsets.size(); i++) {
            System.out.print(test.itemsets.get(i)[0]);
            System.out.print(" \n");
        }
    }
}

class HashTrie {
    private HashMap<int[], Integer> root;
 
    public HashTrie() {
       root = new HashMap<int[], Integer>();
    }
    public HashTrie(ArrayList<int[]> candidates) {
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
 
    public ArrayList<int[]> generateNewItemsets(ArrayList<ArrayList<Integer>> transactions) {
        HashMap<int[], Integer> currentNode = root;
        ArrayList<int[]> newItemsets = null;
        boolean safe = false;
        for (int[] key : currentNode.keySet()) {
            for (int i = 0; i < transactions.size(); i++) {
                for(int j = 0; j < key.length; j++) {
                    if(transactions.get(i).get(j) == key[j]) {
                        safe = true;
                        break;
                    }
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
