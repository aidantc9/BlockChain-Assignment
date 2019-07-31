import java.util.*;
import java.lang.*;
import java.io.*;
import java.sql.*;
public class Block {
	private int index; // the index of the block in the list
	private java.sql.Timestamp timestamp; // time at which transaction
 	// has been processed
	private Transaction transaction; // the transaction object
	private String nonce; // random string (for proof of work)
	private String previousHash; // previous hash (set to "" in first block)
	//(in first block, set to string of zeroes of size of complexity "00000")
	private String hash; // hash of the block (hash of string obtained from previous variables via toString() method)
	private int numHashTry;

	public Block (int index, Transaction transaction, String previousHash) throws UnsupportedEncodingException{//Contructor 1 for Block given an index, a transaction and the previous hash value
 		this.index =index;
 		this.transaction= transaction;
 		this.previousHash=previousHash;
 		this.timestamp=  new Timestamp(System.currentTimeMillis());
 		Sha1 sha = new Sha1();
 
 		this.nonce="";
 		generateNonce(sha);//Calls the generate Nonce class which like the name implies generates a Nounce given a has object and the possible exception is thrown

 		this.hash= sha.hash(this.toString());//generates a hash for the block given all the values 
 	}
 	public Block (int index,Timestamp timestamp, Transaction transaction, String nonce, String previousHash, String hash){//Contructor 2 used when reading a file where all the values are given 
 		this.index =index;
 		this.transaction= transaction;
 		this.previousHash=previousHash;
 		this.timestamp =timestamp;
 		this.nonce=nonce;
 		this.previousHash =previousHash;
 		this.hash= hash;

 	}
 	//generateNonce which takes in a has object and then generates a nonce 
 	public void generateNonce (Sha1 sha) throws UnsupportedEncodingException {// throws the possible acception caused by the hasing 
 		boolean check=false;
 		this.numHashTry=0;

 		do{

 			this.nonce= randomS();//calls the random String method which generates a random string that is to be hashed
 			String temp = sha.hash(this.toString());//hasing the string 
 			check=true;
 			int i=0;
 			while (check==true && i<4){//checks if the first 4 characters of the hash are zeroes 
 				if (temp.charAt(i)!='0'){
 					check= false;
 				}
 				i++;

 			}
 			numHashTry++;

 		} while (check!=true);

 		System.out.println("The number of hash trials needed to find this nonce was "+numHashTry);
 	}
 	//generates a random string for the Nounce
 	protected String randomS(){
 		String visChar = " ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz!@#$%^&*()_+{}|:<>?-=[];'./`~'\"\"" ;//String containing all visible chars
 		StringBuilder ans = new StringBuilder();
 		Random rnd = new Random();
 		while (ans.length() < 10) { // Generates a random string with length given by the number in the while loop
            int i = (int) (rnd.nextFloat() * visChar.length());
            ans.append(visChar.charAt(i));
        }
        String ansS = ans.toString();
        return ansS;
 	}
 	//getter methods for accesing data 
 	public int getIndex(){
 		return this.index;
 	}
 	public Transaction getTrans(){
 		return this.transaction;
 	}
 	public String getNonce(){
 		return this.nonce;
 	}
 	public String getPreHash(){
 		return this.previousHash;
 	}
 	public String getHash(){
 		return this.hash;
 	}
 	public Timestamp getTime(){
 		return this.timestamp;
 	}

	// To string used to print blocks as well as to generated the hash 

 	public String toString() {
 		return timestamp.toString() + ":" + transaction.toString()+ "." + nonce+ previousHash;
 		
	}
}