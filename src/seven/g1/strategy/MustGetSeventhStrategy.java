package seven.g1.strategy;

import java.util.ArrayList;
import java.util.List;

import seven.g1.G1Player;
import seven.g1.bean.Statistics;
import seven.ui.Letter;

public class MustGetSeventhStrategy implements BidStrategy {

	@Override
	public int getBid( Letter bidLetter, ArrayList<Character> currentLetters, 
			Statistics stats, int defenseFactor ) {
		// If an additional letter will get us a ~50 point bonus, we can spend up to ~50 points to get it!
		List<Character> list = new ArrayList<Character>(currentLetters);
		// Add new letter
		list.add(bidLetter.getCharacter());
		// See if bid letter will increase our score
		int beforeScore = G1Player.getBestScore(currentLetters);
		int afterScore = G1Player.getBestScore(list);
		int benefit = 0;
		if ( afterScore > beforeScore  ) {
			benefit = afterScore - beforeScore;
		}
		if ( benefit >= 50 ) {
			// Scale by the amount of turns left.
			return benefit- stats.turnsLeft();
		} else {
			return 1+defenseFactor;
		}	
	}

}
