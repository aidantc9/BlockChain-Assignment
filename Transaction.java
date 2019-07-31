public class Transaction {
 	private String sender;
 	private String receiver;
 	private int amount;

	public Transaction (String sender, String receiver, int amount){//Contructor for the trasaction class that intializes all the variables 
		this.sender=sender;
		this.receiver=receiver;
		this.amount=amount;
	}
	//getter methods to access the data 
	public String getSender(){
 		return this.sender;
 	}
 	public String getReceiver(){
 		return this.receiver;
 	}
 	public int getAmount(){
 		return this.amount;
 	}
 	//toString for the Transaction class to allow the object to be printed 
	public String toString(){
		return sender + ":" + receiver + "=" + amount;
	}
}