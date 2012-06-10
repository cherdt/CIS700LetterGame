package seven.g1;

import java.util.ArrayList;
import java.util.List;

import seven.ui.Letter;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class MustGetSeventhStrategy implements BidStrategy {

	private Letter bidLetter;
	private ArrayList<Character> currentLetters;
	private int defenseFactor;
	
	public MustGetSeventhStrategy( Letter bidLetter, ArrayList<Character> currentLetters, int defenseFactor ) {
		this.bidLetter = bidLetter;
		this.currentLetters = currentLetters;
		this.defenseFactor = defenseFactor;
	}
	
	@Override
	public int getBid() {
		// If an additional letter will get us a ~50 point bonus, we can spend up to ~50 points to get it!
		List<Character> list = new ArrayList<Character>(this.currentLetters);
		// Add new letter
		list.add(this.bidLetter.getCharacter());
		// See if bid letter will increase our score
		int beforeScore = G1Player.getBestScore(currentLetters);
		int afterScore = G1Player.getBestScore(list);
		int benefit = 0;
		if ( afterScore > beforeScore  ) {
			benefit = afterScore - beforeScore;
		}
		if ( benefit >= 50 ) {
			return benefit-1;
		} else {
			return 1+defenseFactor;
		}	
	}

}
