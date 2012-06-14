package seven.g1.strategy;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import seven.g1.bean.Statistics;
import seven.ui.Letter;

public class FewLettersStrategy implements BidStrategy {
	
	// for logging
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public int getBid( Letter bidLetter, ArrayList<Character> currentLetters, 
			Statistics stats, int defenseFactor ) {
		int turns = (stats.getOpponents().size()+1) * (8 - stats.getSecretLetterCount());
		double ratio = turns/35d * .1;
		double factor = 1;
		/* 
		 * Why I got rid of the ratio/factor:
		 * This was only effective when one of the opponents was LetterPlayer or the G5 player,
		 * both of which ended with far fewer than 7 letters. This freed up other letters that
		 * smarter players didn't really want or need, and we picked them up cheap. Assuming G5
		 * fixes their player, this strategy totally fails--we never get our 7-letter bonus.
		 * Better to be aggressive and insure the bonus.
		 */
		
//		if ( stats.getOpponents().get(0) != null ) {
//			//ratio = (1.0/(double)turns)*Math.pow((1+ratio), stats.getOpponents().get(0).getBids().size());
//			ratio = (4.0/(double)turns);
//			factor = ratio*stats.getOpponents().get(0).getBids().size();
//		}
//		logger.trace("Turns:" + turns + ", ratio:" + ratio + ", ratio*bids:" + (ratio * stats.getOpponents().get(0).getBids().size()));
//		logger.trace("Avg bid for " + bidLetter.getCharacter() + " is " + stats.getAverageBid(bidLetter.getCharacter()));

		
		double bid = stats.getAverageBid(bidLetter.getCharacter()) * Math.min(1d, factor);

		/*
		 * This is too restrictive -- we give up on too many letters that could potentially help
		 */
		// TODO Don't bid on something that can't possibly help
		/*
		if ( stats.getStatistics(bidLetter.getCharacter() ) == 1 ) {
			bid = 1;
		}
		*/
		
		if ( bid < .5 ) {
			bid = 1;
		}
		return (int) Math.round( bid );
	}

}
