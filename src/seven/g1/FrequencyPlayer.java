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

public class FrequencyPlayer implements Player {

	
	/*
	 * This player bids randomly.
	 */
	
	// an array of words to be used for making decisions
	private static final Word[] wordlist;

	private static final Word[] sevenLetterWords;
	// for logging
	private Logger logger = Logger.getLogger(this.getClass());

	// the set of letters that this player currently has
	private ArrayList<Character> currentLetters;
	
	// unique ID
	private int myID;
	
	// for generating random numbers
	private Random random = new Random();
	
	private Statistics stats;
	/* This code initializes the word list */
	static {
		BufferedReader r;
		String line = null;
		ArrayList<Word> wtmp = new ArrayList<Word>(55000);
		ArrayList<Word> seven = new ArrayList<Word>(27000);
		try {
			// you can use textFiles/dictionary.txt if you want the whole list
			r = new BufferedReader(new FileReader("textFiles/super-small-wordlist.txt"));
			while (null != (line = r.readLine())) {
				wtmp.add(new Word(line.trim()));
				String[] split = line.split(","); 
				if (split.length==2 && split[0].trim().length() == 7)
				{
					seven.add(new Word(line.trim()));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wordlist = wtmp.toArray(new Word[wtmp.size()]);
		sevenLetterWords = seven.toArray(new Word[seven.size()]);
	}

	
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
		Collections.addAll(word, sevenLetterWords);
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
		List<Character> list = new ArrayList<Character>(currentLetters);

		// Defense factor is to prevent other players from bidding high
		// but getting letters cheaply
		int defenseFactor = 0;
		// Current idea -- if any opponent is bidding, on average, 10 greater
		// than the letter value -- increase Defense Factor to 7
		List<Opponent> opponents = new ArrayList<Opponent>(playerList.size());
		int id = 0;
		// Initialize opponents
		for ( String playerName : playerList ) {
			opponents.add( new Opponent() );
			opponents.get(id).setId(id);
			id++;
		}
		id = 0;
		// Iterate over bidding rounds
		for ( PlayerBids round : playerBidList ) {
			Letter targetLetter = round.getTargetLetter();
			for ( int bid : round.getBidvalues() ) {
				opponents.get(id).addBid(new Bid(bid,targetLetter.getValue(),targetLetter.getCharacter()));
			}
		}
		// Iterate over opponents
		for ( Opponent opponent : opponents ) {
			// Tried 10 at first, but Random Player slips under it
			if ( opponent.getAverageOverValue() > 5 ) {
				defenseFactor = 7;
				break;
			}
		}
		logger.trace("Defense factor: " + defenseFactor);
		
		
		if (list.size() < 3 && bidLetter.getValue() < 4)
		{
			return bidLetter.getValue()+ (int)Math.round(Math.random()*2) + defenseFactor;
		}
		
		double st = stats.getStatistics(bidLetter.getCharacter());
		
		return (int)Math.round(3d * (1- st)) + bidLetter.getValue() + defenseFactor;
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
		}
		String s = new String(c);
		Word ourletters = new Word(s);
		Word bestword = new Word("");
		for (Word w : wordlist) {
			if (ourletters.contains(w)) {
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
