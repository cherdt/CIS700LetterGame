package seven.g1;

import java.util.ArrayList;
import java.util.List;

import seven.ui.Letter;

public class Opponent 
{
	private List<Bid> bids = new ArrayList<Bid>();
	private List<Letter> letters = new ArrayList<Letter>();
	private int id;
	
	public List<Bid> getBids() {
		return bids;
	}
	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	public void addBid( Bid bid ) {
		this.bids.add(bid);
		if ( this.bids.size() > 3 ) {
			this.bids.remove(0);
		}
	}
	
	public void addLetter( Letter letter ) {
		this.letters.add(letter);
	}
	
	public int getLetterCount() {
		return this.letters.size();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public double getAverageOverValue() {
		int total = 0;
		// handle trivial case
		if ( this.bids.size() == 0 ) {
			return 0;
		}
		// Iterate over bids and calculate payment above letter value
		for ( Bid b : this.bids ) {
			total += (b.getBid()-b.getValue());
		}
		return total/this.bids.size();
	}
	
}
