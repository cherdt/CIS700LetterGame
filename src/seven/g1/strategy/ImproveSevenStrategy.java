package seven.g1.strategy;

import java.util.ArrayList;
import java.util.List;

import seven.g1.G1Player;
import seven.g1.bean.Statistics;
import seven.ui.Letter;

public class ImproveSevenStrategy implements BidStrategy {

	@Override
	public int getBid( Letter bidLetter, ArrayList<Character> currentLetters, 
			Statistics stats, int defenseFactor ) {
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
		if ( bidLetter.getValue() < benefit ) {
			return benefit-stats.turnsLeft();
		} else {
			return 2;
		}	
	}

}
