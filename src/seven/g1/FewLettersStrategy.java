package seven.g1;

import seven.ui.Letter;

public class FewLettersStrategy implements BidStrategy {

	private Letter bidLetter;
	private int defenseFactor;
	
	public FewLettersStrategy( Letter bidLetter, int defenseFactor ) {
		this.bidLetter = bidLetter;
		this.defenseFactor = defenseFactor;
	}
	
	@Override
	public int getBid() {
		if ( this.bidLetter.getValue() < 4 ) {
			// For common, low-value, (assumed) generally useful letters, bid the value plus a random factor
			return bidLetter.getValue()+ (int)Math.round(Math.random()*3) + defenseFactor;
		} else {
			// It's risky to bid up uncommon letters early on, so bid face value
			return bidLetter.getValue()+1;
		}
	}

}
