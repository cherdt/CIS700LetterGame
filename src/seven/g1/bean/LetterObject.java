package seven.g1.bean;

import seven.ui.Letter;

public class LetterObject extends Letter
{
	private double stats;
	private int count;
	public LetterObject(char c, int score, int count)
	{
		super(c, score);
		
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
	
	
	
}
