package seven.g1;

import java.util.ArrayList;

import seven.ui.Letter;

public interface BidStrategy {
	public int getBid( Letter bidLetter, ArrayList<Character> currentLetters, 
						Statistics stats, int defenseFactor );
}
