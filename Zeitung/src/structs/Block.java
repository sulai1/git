package structs;

import java.util.LinkedList;

import algorithm.Multimap;

public class Block extends LinkedList<Transaction>{

	private static final long serialVersionUID = 1L;
	
	private Multimap<Long, Transaction> transactions = new Multimap<Long, Transaction>(t -> t.getId());
	
	public void add(int index, Transaction element) {
		
	}
}
