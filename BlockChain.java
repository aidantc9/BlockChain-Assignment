import java.util.*;
import java.io.*;
import java.sql.*;
public class BlockChain{
	private ArrayList<Block> aL;
	private int currentIndex;

	public BlockChain(){//contructor for the blockchain
		this.aL= new ArrayList<Block>();//the array list holding the block
		this.currentIndex=0;// the current index starting at zero
	}
	//This method is used to add a block to the blockchain given a transaction
	public void add (Transaction t) throws UnsupportedEncodingException{//throws the possible exception caused by the hasing method
		String preHash;
		if (currentIndex==0)//if its the first element it sets the prehash to 00000
			preHash="00000";
		
		else{//otherwise it finds the previous hash 
			preHash= aL.get(currentIndex).getHash();
		}
		
		currentIndex++;	
		
		
		Block b = new Block (currentIndex,t,preHash);
		aL.add(b);


	}
	//setter for the index
	public void setCurrentIndex(int i){
		 this.currentIndex=i;
	}
	//increments the index by 1
	public void incCurIndex(){
		 this.currentIndex++;
	}
	//getters for the index and list
	public int getCurIndex(){
 		return this.currentIndex;
 	}
	public ArrayList<Block> getList(){
		return this.aL;
	}
	//this read in a blockchain from a given file and creates a new block chain
	public static BlockChain fromFile (String filename) throws FileNotFoundException{//Throws the file not found exception if a file is not found
		File file = new File (filename);
		Scanner sc = new Scanner (file);
		BlockChain bc = new BlockChain();
		while (sc.hasNextLine()){
			//Alls these variables are read in from the file and set to a temo value
			bc.setCurrentIndex(Integer.parseInt(sc.nextLine()));
			Timestamp timestamp=  new Timestamp (Long.parseLong(sc.nextLine()));
			String sender = sc.nextLine();
			String receiver = sc.nextLine();
			int amount = Integer.parseInt(sc.nextLine());
			String nonce = sc.nextLine(); 
			String hash= sc.nextLine();
			String preHash="";
			if (bc.getCurIndex()==0)//same as before if first element then it sents prehash to 00000 otherwise it reads in the last hash
				preHash="00000";
			
			else{
				preHash= bc.getList().get(bc.getCurIndex()-1).getHash();
			}

			//bc.setCurrentIndex(index);//increase the index of the blockchain by 1
			Transaction t = new Transaction (sender,receiver,amount);
			Block b = new Block (bc.getCurIndex(),timestamp,t,nonce,preHash,hash);
			bc.getList().add(b);


		}
		return bc;
	}
	//Writes a blockchian to a file 
	public void toFile(String fileName) throws IOException,FileNotFoundException{// throws a exception if the file does not exsist or cant be written to 
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		for (int i =0; i<this.aL.size();i++){//writes all the values of the blockchain to the file 
			Block tempB = this.aL.get(i);
			Transaction tempT = tempB.getTrans();
			writer.println(Integer.toString(i));
			writer.println(Long.toString(tempB.getTime().getTime()));
			writer.println(tempT.getSender());
			writer.println(tempT.getReceiver());
			writer.println(Integer.toString(tempT.getAmount()));
			writer.println(tempB.getNonce());
			writer.println(tempB.getHash());
		}
		writer.close();
		
	}
	//Validates the entire blockchain making sure that all the values for the blockchain are consistent 
	public boolean validateBlockchain() throws UnsupportedEncodingException{//throws a exception if the has does not work
		
		Sha1 sha = new Sha1();//the hash object
		
		for (int i = 0; i<this.aL.size();i++){//goes through every block in the chain and checks the values 
			Block tempB= this.aL.get(i);
			String checkHash = tempB.getHash();
			String correctHash= sha.hash(tempB.toString());
			String tempPreHash="";
			String currentUser ="";
			if (tempB.getIndex()==0){
				tempPreHash="00000";
				currentUser= tempB.getTrans().getSender();
			

			}
			else{
				tempPreHash= this.aL.get(tempB.getIndex()-1).getHash();
				currentUser= tempB.getTrans().getReceiver();

			}

			if (checkHash.equals(correctHash)==false || i!=tempB.getIndex() || tempB.getPreHash().equals(tempPreHash)==false){//checks if the hash,index, and prehash are correct
				
				return false;
			}
			if (this.getBalance(currentUser)<0){//checks that all the users are not spending bitcoins they dont have 
				return false;
			}

		}
		
		return true;

	}
	//This method get the balance of given user allowing other methods to check if someone in the chain is using bitcoins they dont have
	public int getBalance(String username){
		int totalStore=0;
		for (int i = 0; i<this.aL.size();i++){//goes through the blockchain
			Block tempB= this.aL.get(i);
			if (i==0 &&tempB.getTrans().getSender().equals(username)){//if its the first element it sets the total amount of bit coins of that "user" to that value
				totalStore=tempB.getTrans().getAmount();
			}
			else if (tempB.getTrans().getReceiver().equals(username)){//otherwise it checks when the user in check gets bitcoins it adds to the totalamount they have 
				totalStore+=tempB.getTrans().getAmount();
			}
			if (tempB.getTrans().getSender().equals(username)){//whenever the user spends his coins then his total bitcoins stored decreases 
				totalStore-=tempB.getTrans().getAmount();
			}

		}
		return totalStore;


	}
	//To string for the block chain
	public String toString() {
 		return this.aL.toString();
 		
	}




	//main method used to test all the methods made for the blockchain
	public static void main (String[] args) throws UnsupportedEncodingException, FileNotFoundException,IOException{ //throws all possible exception that could occur
		BlockChain b = new BlockChain();
		Scanner sc = new Scanner(System.in);
		boolean check = true;
		boolean check1 = true;
		boolean fileCheck=false;
		boolean valid=false;
		String sender="";
		String receiver="";
		String fileN="";
		String ans="";
		int amount=0;
		while (fileCheck==false){//continues to ask the user to put in the correct value 
			try{
				System.out.println("Please enter a the File name include the directory"); 
				fileN = sc.nextLine();//get the file name
				b=b.fromFile(fileN);//reads in a blockchain from a file 
				fileCheck=true;
			}catch(Exception E){
				System.out.println("Please enter in a valid file name");
			
			}
		}
		System.out.println("It is "+b.validateBlockchain()+" that the given BlockChain is valid");
		while (check1==true && b.validateBlockchain()==true){//loop that forces the user to enter in yes or no
				System.out.println("Would you like to add another Transaction to this BlockChain if no enter no if yes enter yes");
				ans = sc.nextLine();
				ans= ans.trim();
				if (ans.toLowerCase().equals("no")){
					check1=check=false;
				}
				if(ans.toLowerCase().equals("yes")){
					check1=false;
				}
		}

		while (check==true && b.validateBlockchain()==true){//if the blockchain is valid then this loop will run 
			check1=true;
			valid=false;
			System.out.println("Please enter a the sender of the Transaction that you wish to be added to the BlockChain");
			sender = sc.nextLine();
			System.out.println("Please enter a the receiver of the Transaction that you wish to be added to the BlockChain");
			receiver = sc.nextLine();
			while(valid==false){//makes sure the user enters in a int
			System.out.println("Please enter a the amount of the Transaction that you wish to be added to the BlockChain");
				try{
					amount=Integer.parseInt(sc.nextLine());
					valid=true;
				}catch (Exception E){
					System.out.println("Please enter a valid amount");
				}
			}
			if (b.getBalance(sender)>0 &&b.getBalance(sender)-amount>=0){//if the transaction is valid then it will be added to the blockchain
				Transaction t = new Transaction (sender,receiver,amount);
				b.add(t);
			}
			else{
				System.out.println("The Transaction was not valid and therefore was not added");
			}
			while (check1==true && b.validateBlockchain()==true){//loop that forces the user to enter in yes or no
				System.out.println("Would you like to add another Transaction to this BlockChain if no enter no if yes enter yes");
				ans = sc.nextLine();
				ans= ans.trim();
				if (ans.toLowerCase().equals("no")){
					check1=check=false;
				}
				if(ans.toLowerCase().equals("yes")){
					check1=false;
				}

			}
			System.out.println("It is "+b.validateBlockchain()+" that the given BlockChain is valid");





		}
		if (b.validateBlockchain()==true){
			b.toFile(fileN.substring(0,fileN.length()-4)+"_achar209.txt");//adds the block chain to a file
		}




	}
}