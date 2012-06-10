package seven.g1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.*;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.LetterGame;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;
import seven.g1.Bid;
import seven.g1.Opponent;

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
	
	// for generating random numbers
	private Random random = new Random();
	
	private Statistics stats;


	
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
		stats = new Statistics(secretState, word);
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
		BidStrategy strategy = new StatsStrategy(bidLetter, stats, defenseFactor);
		logger.trace("Using Stats Strategy (default)");

		// If we already have a 7 letter word...
		if ( getWord().length() >= 7 ) {
			strategy = new ImproveSevenStrategy( bidLetter, this.currentLetters );
			logger.trace("Switching to ImproveSevenStrategy");
		} else {
			// If we have 6 letters and desperately want a seventh...
			if ( currentLetters.size() >= 6 ) {
				strategy = new MustGetSeventhStrategy( bidLetter, this.currentLetters, defenseFactor );
				logger.trace("Switching to MustGetSeventhStrategy");
			}
			
			// If we only have a few letters...
			if (currentLetters.size() < 3 )	{
				strategy = new FewLettersStrategy(bidLetter, defenseFactor);
				logger.trace("Switching to FewLettersStrategy");
			}
		}
		
		return strategy.getBid();
	}


	/*
	 * Defense factor is to prevent other players from bidding high
	 * but getting letters cheaply
	 */
	private int getDefenseFactor( ArrayList<PlayerBids> playerBidList, ArrayList<String> playerList, int numOfSecretLetters ) {
		
		int defenseFactor = 0;
		int overbidTolerance = 5;
		// Current idea -- if any opponent is bidding, on average, greater than the
		// letter value + the overbid tolerance, increase the Defense Factor to 50/(7-# of secret letters)
		List<Opponent> opponents = new ArrayList<Opponent>(playerList.size());
		int id = 0;
		// Initialize opponents
		for ( String playerName : playerList ) {
			opponents.add( new Opponent() );
			opponents.get(id).setId(id);
			id++;
		}

		// Iterate over bidding rounds
		for ( PlayerBids round : playerBidList ) {
			Letter targetLetter = round.getTargetLetter();
			id = 0;
			for ( int bid : round.getBidvalues() ) {
				opponents.get(id).addBid(new Bid(bid,targetLetter.getValue(),targetLetter.getCharacter()));
				id++;
			}
		}
		// Iterate over opponents
		for ( Opponent opponent : opponents ) {
			if ( opponent.getId() != this.myID ) {
				// Tried 10 at first, but Random Player slips under it
				// && opponent.getBids().size() >= 3
				if ( opponent.getAverageOverValue() > overbidTolerance ) {
					// A random +/-2 to make less unpredictable
					defenseFactor = 50/(7-numOfSecretLetters) + (int)Math.round(Math.random()*4)-2;
					break;
				}
			}
		}
		logger.trace("Defense factor: " + defenseFactor);

		return defenseFactor;
	}
	
	
	/*
	 * This method is called after a bid. It indicates whether or not the player
	 * won the bid and what letter was being bid on, and also includes all the
	 * other players' bids. 
	 */
    public void bidResult(boolean won, Letter letter, PlayerBids bids) {
    	stats.removeChar(letter.getCharacter());
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
