package seven.g0;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.LetterGame;
import seven.ui.Player;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class RandomPlayer implements Player {

	
	/*
	 * This player bids randomly.
	 */

	// an array of words to be used for making decisions
	private static final Word[] wordlist;

	// for logging
	private Logger logger = Logger.getLogger(this.getClass());

	// the set of letters that this player currently has
	private ArrayList<Character> currentLetters;
	
	// unique ID
	private int myID;
	
	// for generating random numbers
	private Random random = new Random();
		
	
	/* This code initializes the word list */
	static {
		BufferedReader r;
		String line = null;
		ArrayList<Word> wtmp = new ArrayList<Word>(55000);
		try {
			// you can use textFiles/dictionary.txt if you want the whole list
			r = new BufferedReader(new FileReader("textFiles/super-small-wordlist.txt"));
			while (null != (line = r.readLine())) {
				wtmp.add(new Word(line.trim()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wordlist = wtmp.toArray(new Word[wtmp.size()]);
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
		//logger.trace("myID=" + myID + " and I'm bidding on " + bidLetter);
		//logger.trace("myID= " + myID + " and my score is " + secretState.getScore());

		// randomly bid up to half of the remaining points
		return random.nextInt(secretState.getScore()/2);
	}

	
	/*
	 * This method is called after a bid. It indicates whether or not the player
	 * won the bid and what letter was being bid on, and also includes all the
	 * other players' bids. 
	 */
    public void bidResult(boolean won, Letter letter, PlayerBids bids) {
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
		char c[] = new char[currentLetters.size()];
		for (int i = 0; i < c.length; i++) {
			c[i] = currentLetters.get(i);
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
