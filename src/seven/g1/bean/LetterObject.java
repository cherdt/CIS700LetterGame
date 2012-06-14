package seven.g1.bean;

import seven.ui.Letter;

public class LetterObject extends Letter
{
	private double stats;
	private int count;
	
	private int bidCount;
	private int bidTotal;
	
	public LetterObject(char c, int score, int count, int bidCount, int bidTotal)
	{
		super(c, score);
		this.bidCount = bidCount;
		this.bidTotal = bidTotal;
		this.count = count;
	}
	
	public double getStats() {
		return stats;
	}
	public void setStats(double stats) {
		this.stats = stats;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getBidCount() {
		return bidCount;
	}

	public void setBidCount(int bidCount) {
		this.bidCount = bidCount;
	}

	public int getBidTotal() {
		return bidTotal;
	}

	public void setBidTotal(int bidTotal) {
		this.bidTotal = bidTotal;
	}
	
	
	
}
