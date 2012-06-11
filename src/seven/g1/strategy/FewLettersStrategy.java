package seven.g1.strategy;

import java.util.ArrayList;

import seven.g1.bean.Statistics;
import seven.ui.Letter;

public class FewLettersStrategy implements BidStrategy {
	
	@Override
	public int getBid( Letter bidLetter, ArrayList<Character> currentLetters, 
			Statistics stats, int defenseFactor ) {
		if ( bidLetter.getValue() < 4 ) {
			// For common, low-value, (assumed) generally useful letters, bid the value plus a random factor
			return bidLetter.getValue()+ (int)Math.round(Math.random()*3) + defenseFactor;
		} else if (bidLetter.getValue() < 6) {
			return bidLetter.getValue();
				
		} else {
			// It's risky to bid up uncommon letters early on, so bid face value
			return bidLetter.getValue()-4;
		}
	}

}
