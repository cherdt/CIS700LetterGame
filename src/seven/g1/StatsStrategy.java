package seven.g1;

import seven.ui.Letter;

public class StatsStrategy implements BidStrategy {

	private Letter bidLetter;
	private Statistics stats;
	private int defenseFactor;

	public StatsStrategy( Letter bidLetter, Statistics stats, int defenseFactor ) {
		this.bidLetter = bidLetter;
		this.stats = stats;
		this.defenseFactor = defenseFactor;
	}
	
	@Override
	public int getBid() {
		double st = stats.getStatistics(bidLetter.getCharacter());
		
		// TODO update this calculation. We might want a negative factor for letters than currently produce stat factor zero?
		int statFactor = (int)Math.round(3d * (1- st));
		// Don't bid on something that won't help at all
		if ( st == 1 ) {
			return 1;
		}
		return statFactor + bidLetter.getValue() + defenseFactor;
	}

}
