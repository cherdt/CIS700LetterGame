package seven.g1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;
import seven.g1.bean.Bid;
import seven.g1.bean.Opponent;
import seven.g1.bean.Statistics;
import seven.g1.bean.Word;
import seven.g1.strategy.BidStrategy;
import seven.g1.strategy.FewLettersStrategy;
import seven.g1.strategy.ImproveSevenStrategy;
import seven.g1.strategy.MustGetSeventhStrategy;
import seven.g1.strategy.StatsStrategy;

public class WhattaPlayer extends G1Player implements Player {

	
	/*
	 * This player uses different bidding strategies based on the game state.
	 */
	

	// for logging
	private Logger logger = Logger.getLogger(this.getClass());

	// the set of letters that this player currently has
	private ArrayList<Character> currentLetters;
	
	// unique ID
	private int myID;
	
	private Statistics stats;

	private BidStrategy fewLettersStrategy = new FewLettersStrategy();
	private BidStrategy improveSevenStrategy = new ImproveSevenStrategy();
	private BidStrategy mustGetSeventhStrategy = new MustGetSeventhStrategy();
	private BidStrategy statsStrategy = new StatsStrategy();
    /*
     * This is called once at the beginning of a Game.
     * The id is what the game considers to be your unique identifier
     * The number_of_rounds is the total number of rounds to be played in this game
     * The number_of_players is, well, the number of players.
     */
	public void newGame(int id, int number_of_rounds, int number_of_players) {
		myID = id;
	}


	/*
	 * This method is called at the beginning of a new round.
	 * The secretState contains your current score and the letters that were secretly given to you in this round
	 * The current_round indicates the current round number (0-based)
	 */
	public void newRound(SecretState secretState, int current_round) {
		ArrayList<Word> word = new ArrayList<Word>();
		Collections.addAll(word, G1Player.sevenLetterWords);
		stats = new Statistics(secretState, word, myID);
		// be sure to reinitialize the list at the start of the round
		currentLetters = new ArrayList<Character>();
		
		// add any letters from the secret state
		for (Letter l : secretState.getSecretLetters()) {
			//logger.trace("myID = " + myID + " and I'm adding " + l + " from the secret state");
			currentLetters.add(l.getCharacter());
		}
	}
	

	/*
	 * This method is called when there is a new letter available for bidding.
	 * bidLetter = the Letter that is being bid on
	 * playerBidList = the list of all previous bids from all players
	 * playerList = the class names of the different players
	 * secretState = your secret state (which includes the score)
	 */
	public int getBid(Letter bidLetter, ArrayList<PlayerBids> playerBidList, ArrayList<String> playerList, SecretState secretState) {

		// Defense factor is to prevent other players from bidding high
		// but getting letters cheaply
		int defenseFactor = getDefenseFactor(playerBidList,playerList, secretState.getSecretLetters().size() );

		// Default strategy is to play the statistics
		// TODO if we happen to pick up Z/X/Q etc., the player basically gives up all hope. Why not ignore it and gun for an 8th letter?
		BidStrategy strategy = statsStrategy;
		logger.trace("Using Stats Strategy (default)");

		// If we already have a 7 letter word...
		if ( getWord().length() >= 7 ) {
			strategy = improveSevenStrategy;
			logger.trace("Switching to ImproveSevenStrategy");
		} else {
			// If we have 6 letters and desperately want a seventh...
			if ( currentLetters.size() >= 6 ) {
				strategy = mustGetSeventhStrategy;
				logger.trace("Switching to MustGetSeventhStrategy");
			}
			
			// If we only have a few letters...
			if (currentLetters.size() < 3 )	{
				strategy = fewLettersStrategy;
				logger.trace("Switching to FewLettersStrategy");
			}
		}
		
		return strategy.getBid(bidLetter, currentLetters, stats, defenseFactor);
	}


	/*
	 * Defense factor is to prevent other players from bidding high
	 * but getting letters cheaply
	 */
	public int getDefenseFactor( ArrayList<PlayerBids> playerBidList, ArrayList<String> playerList, int numOfSecretLetters ) {
		
		int defenseFactor = 0;
		int overbidTolerance = 5;
		stats.initPlayers(playerList);
		// Iterate over opponents
		for ( Opponent opponent : stats.getOpponents() ) {
			// Tried 10 at first, but Random Player slips under it
			// && opponent.getBids().size() >= 3
			if ( opponent.getAverageOverValue() > overbidTolerance ) {
				// A random +/-2 to make less unpredictable
				defenseFactor = 50/(7-numOfSecretLetters) + (int)Math.round(Math.random()*4)-2;
				break;
			}
		}
		logger.trace("Defense factor: " + defenseFactor + " turnsLeft = "+stats.turnsLeft());

		return defenseFactor;
	}
	
	
	/*
	 * This method is called after a bid. It indicates whether or not the player
	 * won the bid and what letter was being bid on, and also includes all the
	 * other players' bids. 
	 */
    public void bidResult(boolean won, Letter letter, PlayerBids bids) {
    	stats.removeChar(letter.getCharacter());
		stats.updateBids(letter, bids);
    	if (won) {
    		//logger.trace("My ID is " + myID + " and I won the bid for " + letter);
    		currentLetters.add(letter.getCharacter());
    		stats.updateCharStats(letter.getCharacter());
    	}
    	else {
    		//logger.trace("My ID is " + myID + " and I lost the bid for " + letter);
    	}
    }

    /*
     * This method is called after all the letters have been purchased in the round.
     * The word that you return will be scored for this round.
     */
	public String getWord() {
		return getBestWord(currentLetters);
	}

	private String getBestWord(List<Character> letters)
	{
		char c[] = new char[letters.size()];
		for (int i = 0; i < c.length; i++) {
			c[i] = letters.get(i);
			logger.trace("Letter " + c[i] + " is in our bag");
		}
		String s = new String(c);
		Word ourletters = new Word(s);
		Word bestword = new Word("");
		logger.trace("Ourletters: " + ourletters.word);
		for (Word w : wordlist) {
			if (ourletters.contains(w)) {
				logger.trace("Found word: " + w.word + " with score " + w.score);
				logger.trace("Compare to best word score: " + bestword.score);
				if (w.score > bestword.score) {
					bestword = w;
				}

			}
		}
		logger.trace("My ID is " + myID + " and my word is " + bestword.word);
		
		return bestword.word;
	}
	
	/*
	 * This method is called at the end of the round
	 * The ArrayList contains the scores of all the players, ordered by their ID
	 */
	public void updateScores(ArrayList<Integer> scores) {
		
	}
}
