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
     private void fim(){
    	int k; 
    	List <int[]> frequent = new ArrayList<int[]>();	//frequency k-1 itemset 
    	Collections.copy(candidates,genCan(1)); 		//generate frequent 1-itemset 
    	
    	for (k=2; candidates.size()!=0 ; k++){
    		frequent.clear(); 
    		frequent=genCan(k);						 	// k candidate set generate from k-1 frequent set 
    		candidates.clear();
    		Collections.copy(candidates,frequent); 		// update candidate itemset for k itemset 
    		
    	}
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

        private ArrayList<int[]> genCan(int k){	//generate k-candidate set 

    	List<int[]>tempcan = new ArrayList<int[]>();
    	int i,j; 
    	int[] temp,newtemp; 
    	if (k==1){						//generate 1-itemset 
    		temp = new int[k];
    		for (i=0;i<999;i++) {
    			temp[0] = i;
    			tempcan.add(temp);
    		}
    	}
    	else if (k==2) {				//generate 2-itemset 
    		temp = new int[k-1];
    		newtemp = new int[k-2];
    		for(i=0;i<candidates.size();i++){
    			for (j=i+1;j<999;j++){
    				temp = candidates.get(i);
    				newtemp[0]=temp[0];
    				newtemp[1]=j; 
    				tempcan.add(newtemp); 
    			}
    		}
    	}
    	else {							//generate k-itemset 
    		int l,m,n,check; 
    		temp = new int[k-1]; 
    		int[] tempcmp = new int[k-1];
    		newtemp = new int[k];
    		
    		for (i=0;i<candidates.size();i++){
    			temp=candidates.get(i);
    			for (j=i+1;j>candidates.size();j++){
    				tempcmp =candidates.get(j);
    				check = 0; 
    				for ( l=0; l<(k-2); l++){
    					if(temp[l]!=tempcmp[l]){
    						break; 
    					}
    					check++; 
    				}
    				if (check==(k-2)){
    					for (m=0;m<temp.length;m++){
    						newtemp[m]=temp[m];
    					}
    					m++; 
    					for(n=(k-2);n<tempcmp.length;n++){
    						newtemp[m++]=tempcmp[n];
    					}
    					tempcan.add(newtemp);
    				}
    			}
    		}
    	}
    	return HashTrie.generateNewItemsets(tempcan);
    }
    public static void main(String[] args)throws Exception {
        Apriori test = new Apriori();
        test.configure(args);
        test.fim(); 				  //calls the actual data mining algorithm fmi
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
 
    public ArrayList<int[]> generateNewItemsets(ArrayList<int[]> candidates) {
        HashMap<int[], Integer> currentNode = root;
        ArrayList<int[]> frequent = null;
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
        frequent.addAll(currentNode.keySet());
        return frequent;  
    }
}
