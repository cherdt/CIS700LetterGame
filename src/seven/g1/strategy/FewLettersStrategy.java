package seven.g1.strategy;

import java.util.ArrayList;

import seven.g1.bean.Statistics;
import seven.ui.Letter;

public class FewLettersStrategy implements BidStrategy {
	
	@Override
	public int getBid( Letter bidLetter, ArrayList<Character> currentLetters, 
			Statistics stats, int defenseFactor ) {
		int turns = stats.getOpponents().size() * (8 - stats.getSecretLetterCount());
		double ratio = turns/35d * .1;
		return (int)Math.round(stats.getAverageBid(bidLetter.getCharacter()) * Math.min(1d, (ratio * stats.getOpponents().get(0).getBids().size())));
	}

}
