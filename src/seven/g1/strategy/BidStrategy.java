package seven.g1.strategy;

import java.util.ArrayList;

import seven.g1.bean.Statistics;
import seven.ui.Letter;

public interface BidStrategy {
	public int getBid( Letter bidLetter, ArrayList<Character> currentLetters, 
						Statistics stats, int defenseFactor );
}
