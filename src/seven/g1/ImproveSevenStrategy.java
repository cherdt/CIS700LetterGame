package seven.g1;

import java.util.ArrayList;
import java.util.List;

import seven.ui.Letter;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class ImproveSevenStrategy implements BidStrategy {

	private Letter bidLetter;
	private ArrayList<Character> currentLetters;

	
	public ImproveSevenStrategy( Letter bidLetter, ArrayList<Character> currentLetters ) {
		this.bidLetter = bidLetter;
		this.currentLetters = currentLetters;
	}
	
	@Override
	public int getBid() {
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
		if ( bidLetter.getValue() < benefit ) {
			return benefit-1;
		} else {
			return 1;
		}	
	}

}
