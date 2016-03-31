package structs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

	private long id; // id of the account
	private BigDecimal cost; // cost of the transaction
	private BigDecimal amount; // amount paid
	private long productID; // product purchased
	private int count; // product count
	private LocalDate date; // timestamp

	class TransactionException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1377318599694699805L;

		public TransactionException() {
			super("Invalid Transaction use!");
		}
		public TransactionException(String string) {
			super("Invalid Transaction use!\n"+string);
		}
	}
	
	public Transaction(long id, BigDecimal cost, BigDecimal amount, long productID, int count) {
		this.id = id;
		this.cost = cost;
		this.amount = amount;
		this.productID = productID;
		this.count = count;
		date = LocalDate.now();
	}

	public Transaction concat(Transaction t) throws TransactionException {
		if(t.getId()!=id)
			throw new TransactionException("concatenation of two transactions with two different accounts");
		long id2 = id;
		BigDecimal amount2 = amount;
		long productID2 = productID;
		int count2 = count;
		return new Transaction(id2, cost, amount2, productID2, count2);
	}

	public BigDecimal getCost() {
		return cost;
	}

	public long getProductID() {
		return productID;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @return the count
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

}
