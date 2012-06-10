package seven.g1;

import java.util.ArrayList;

import seven.ui.Letter;

public class StatsStrategy implements BidStrategy {
	
	@Override
	public int getBid( Letter bidLetter, ArrayList<Character> currentLetters, 
			Statistics stats, int defenseFactor ) {
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
