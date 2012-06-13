package seven.g1.bean;

public class Bid
{
	private int bid;
	private int value;
	private char letter;
	
    public Bid(int bid, int value, char letter)
    {
        this.bid = bid;
        this.value = value;        
        this.letter = letter;
    }
	
	public int getBid() {
		return this.bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public int getValue() {
		return this.value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public char getLetter() {
		return this.letter;
	}
	public void setLetter(char letter) {
		this.letter = letter;
	}
	
	
}
