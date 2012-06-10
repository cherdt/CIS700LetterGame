package seven.g1;

import java.util.ArrayList;

import seven.ui.Letter;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class MustGetSeventhStrategy implements BidStrategy {

	private Letter bidLetter;
	private ArrayList<Character> currentLetters;
	private SecretState secretState;
	
	public MustGetSeventhStrategy( Letter bidLetter, ArrayList<PlayerBids> playerBidList,
			ArrayList<String> playerList, ArrayList<Character> currentLetters,
			SecretState secretState ) {
		this.bidLetter = bidLetter;
		
	}
	
	@Override
	public int getBid() {
		// TODO Auto-generated method stub
		
		// If an additional letter will get us a ~50 point bonus, we can spend up to ~50 points to get it

		// benefit = afterScore - beforeScore
		// bid benefit
		
		return 0;
	}

}
