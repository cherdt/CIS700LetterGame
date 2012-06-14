package seven.g1.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.PlayerBids;
import seven.ui.SecretState;

public class Statistics
{
	public Map<Character, LetterObject> available = new HashMap<Character, LetterObject>();
	//public Map<Character>
	
	//public Map<Integer, Opponents> oponnents
	
	private List<Word> availableWords;
	private Logger logger = Logger.getLogger(this.getClass());
	private List<Character> chars;
	private double[][] pascalsTriangle; // A way to lookup combinations w/out factorials
	private boolean useIgnoreChars = true;
	private final char[] ignoreChars = "JKQXZ".toCharArray(); // There are times we might want to ignore these?
	private List<Character> ignoreList;
	private int myID;
	List<Opponent> opponents;
	private int secretLetterCount;
	
	public Statistics(SecretState state, List<Word> words, int myID)
	{
		secretLetterCount = state.getSecretLetters().size();
		this.myID = myID;
		initTriangle(0);
		ignoreList = new ArrayList<Character>();
		for ( int i = 0; i < ignoreChars.length; i++ ) {
			ignoreList.add(ignoreChars[i]);
		}
		
		chars = new ArrayList<Character>();
		for (Letter l : state.getSecretLetters())
		{
			chars.add(l.getCharacter());
		}
		this.availableWords = words;
		available.put('A', new LetterObject('A', 1, 9, 5, 29));
		available.put('B', new LetterObject('B', 3,2, 5, 35));
		available.put('C', new LetterObject('C', 3,2, 5, 45));
		available.put('D', new LetterObject('D', 2,4, 5, 35));
		available.put('E', new LetterObject('E', 1,12, 5, 31));
		available.put('F', new LetterObject('F', 4,2, 5, 33));
		available.put('G', new LetterObject('G', 2,3, 5, 32));
		available.put('H', new LetterObject('H', 4,2, 5, 45));
		available.put('I', new LetterObject('I', 1,9, 5, 29));
		available.put('J', new LetterObject('J', 8,1, 5, 20));
		available.put('K', new LetterObject('K', 5,1, 5, 29));
		available.put('L', new LetterObject('L', 1,4, 5, 31));
		available.put('M', new LetterObject('M', 3,2, 5, 41));
		available.put('N', new LetterObject('N', 1,6, 5, 30));
		available.put('O', new LetterObject('O', 1,8, 5, 33));
		available.put('P', new LetterObject('P', 3,2, 5, 30));
		available.put('Q', new LetterObject('Q', 10,1, 5, 20));
		available.put('R', new LetterObject('R', 1,6, 5, 27));
		available.put('S', new LetterObject('S', 1,4, 5, 29));
		available.put('T', new LetterObject('T', 1,6, 5, 30));
		available.put('U', new LetterObject('U', 1,4, 5, 25));
		available.put('V', new LetterObject('V', 4,2, 5, 27));
		available.put('W', new LetterObject('W', 4,2, 5, 31));
		available.put('X', new LetterObject('X', 8,1, 5, 25));
		available.put('Y', new LetterObject('Y', 4,2, 5, 33));
		available.put('Z', new LetterObject('Z', 10,1, 5, 20));
		updateCharStats('0');
		
		for (Letter l : state.getSecretLetters())
		{
			removeChar(l.getCharacter());
		}
	}
	
	public void removeChar(Character c)
	{
		LetterObject o = available.get(c);
		o.setCount(o.getCount() - 1);
	}
	
	public void updateCharStats(char c)
	{
		// Build the string of current characters.
		StringBuilder b = new StringBuilder();
		for (Character ch : chars)
		{
			if ( !useIgnoreChars || !ignoreList.contains(ch) ) {
				b.append(ch);
			}
		}
		int maxDistance = Math.min(10 - this.chars.size(), this.turnsLeft() / 2);
		// Add a new character if it exists.
		if (c != '0')
		{
			chars.add(c);
			Word w = new Word(b.toString());

			// Create list/word out of chars still available
			StringBuilder stillAvail = new StringBuilder(100);
			for ( LetterObject o : available.values() ) {
				if ( o.getCount() > 0 ) {
					for ( int i = 0; i<o.getCount(); i++) {
						stillAvail.append(o.getCharacter());
					}
				}
			}
			Word stillAvailLetters = new Word(stillAvail.toString());
			
			// Remove all words that no longer apply.
			Iterator<Word> words = availableWords.iterator();
			while (words.hasNext())
			{
				Word word = words.next();
				
				// Get Edit Distance for each word
				// logger.trace("Edit distance between " + word.word + " & " + w.word + " is " + word.getEditDistance(w));
				
				if (!word.contains(w) || stillAvailLetters.getEditDistance(word) >= maxDistance )
				{
					words.remove();
				}
			}
		}
		// Get the count of words that are lost.
		for (Character chars : available.keySet())
		{
			b.append(chars);
			LetterObject o = available.get(chars);
			o.setStats(0);
			Word w = new Word(b.toString());
			for (Word word : availableWords)
			{
				if (word.getEditDistance(w) >= maxDistance)
				{
					o.setStats(o.getStats()+1);
				}
				
			}
			b.deleteCharAt(b.length()-1);
		}
		// create a percentage for the results.
		for (Character chars : available.keySet())
		{
			LetterObject o = available.get(chars);
			logger.trace(o.getCharacter() + ":" + o.getStats() + " words eliminated");
			int allEliminatedCount = 0;
			if ( availableWords.size() == 0 ) {
				logger.trace("Char " + o.getCharacter() + " loses 100%");
				allEliminatedCount++;
				o.setStats(1);
			} else {
				o.setStats( o.getStats()/ (double) availableWords.size());
			}
			
			if ( allEliminatedCount >= 26 ) {
				// TODO: what if all possibilities are eliminated
				// We need to reconsider--there should be a fallback
				// If we ignore a char, how many words will it bring back (i.e. make possible)?
				logger.trace("All characters have been eliminated!");
			}
			
			logger.trace(o.getCharacter() + ":" + o.getStats());
			
		}
		
		
	}
	
	public double getAverageBid(char c)
	{
		LetterObject o = available.get(c);
		
		return (double)o.getBidTotal() / (double)o.getBidCount();
	}
	
	/**
	 * Add the latest round to the opponent statistics.
	 * 
	 * @param targetLetter
	 * @param round
	 */
	public void updateBids(Letter targetLetter, PlayerBids round)
	{
		int i = 0;
		int id = 0;
		
		// Updating the bid values.
		LetterObject lo = available.get(targetLetter.getCharacter());
    	
    	lo.setBidCount(lo.getBidCount()+1);
    	
    	int maxBid = 0;
    	for (int bid : round.getBidvalues())
    	{
    		if (bid > maxBid && myID != id++)
    		{
    			maxBid = bid;
    		}
    	}
    	lo.setBidTotal(lo.getBidTotal()+maxBid);
		
		
		id = 0;
		// Iterate over bidding rounds
		for ( int bid : round.getBidvalues() ) {
			if (i != myID) {
				opponents.get(id).addBid(new Bid(bid,targetLetter.getValue(),targetLetter.getCharacter()));
				id++;
			}
			i++;
		}
	}
	
	/**
	 * Create the list of players.  Only do this once.
	 * 
	 * @param playerList
	 */
	public void initPlayers(ArrayList<String> playerList)
	{
		initTriangle(playerList.size()*8);
		if (opponents == null)
		{
			opponents = new ArrayList<Opponent>();
			for (int i=0;i<playerList.size();i++)
			{
				// Do not add yourself.
				if (i != myID)
				{
					Opponent o = new Opponent();
					o.setId(i);
					opponents.add(o);
				}
			}
		}
	}
	
	/**
	 * Get the statistical value for the provided character.
	 * 
	 * @param c
	 * @return
	 */
	public double getStatistics(char c)
	{
		return available.get(c).getStats();
	}
	
	/**
	 * Get the list of opponents.
	 * 
	 */
	public List<Opponent> getOpponents()
	{
		return opponents;
	}
	
	/**
	 * Returns the amount of turns left in the round.
	 * 
	 */
	public int turnsLeft()
	{
		if (this.opponents == null)
		{
			return 32;
		}
		int playerCount = this.opponents.size()+1;
		int letters = 8 - secretLetterCount;
		
		int turns = 0;
		for ( int i = 0; i < opponents.size(); i++ )
		{
			if ( myID != opponents.get(i).getId() )
			{
				logger.trace("Player " + i + " has bid " + opponents.get(i).getBids().size() + " times.");
				turns = opponents.get(i).getBids().size();
				break;
			}
		}

		
		return playerCount * letters - turns;
	}
	
	
	public void initTriangle( int s ) {
		pascalsTriangle = new double[s+1][s+1];
		pascalsTriangle[0][0] = 1;
		for ( int i = 1; i <= s; i++ ) {
			for ( int j = 0; j <= s; j++ ) {
				if (j-1 >= 0 ) {
					pascalsTriangle[i][j] = pascalsTriangle[i-1][j] + pascalsTriangle[i-1][j-1];
				} else {
					pascalsTriangle[i][j] = pascalsTriangle[i-1][j];
				}
			}
		}
	}
	
	/*
	 * Returns the number of combinations of n items taken r at a time
	 * Obviously this will give an error if n or r exceed the dimensions of the array
	 */
	public double getComb ( int n, int r ) {
		if ( n < this.pascalsTriangle.length && r < this.pascalsTriangle[0].length )
		{
			return this.pascalsTriangle[n][r];
		}
		else
		{
			return 0.0;
		}
	}

	/*
	 * Returns the probability of r or more occurrences of n trials
	 * given the probability p
	 * TODO: this is not returning values in the expected range
	 */
	public double getProb ( int n, int r, double p ) {
		double prob = 0.0;
		for ( int i = r; i < n; i++ ) {
			prob += (getComb(n,i)/Math.pow(2, n)) * Math.pow(p, i) * Math.pow(1-p, n-i);
		}
		return prob;
	}

	public int getSecretLetterCount() {
		return secretLetterCount;
	}
	
	/*
	 * switch(Character.toLowerCase(bidLetter.getCharacter()))
		{
		case 'a': value = (int)Math.round(8.056331854); break;
		case 'b': value = (int)Math.round(7.264507644); break;
		case 'c': value = (int)Math.round(10.73154693); break;
		case 'd': value = (int)Math.round(8.344566826); break;
		case 'e': value = (int)Math.round(11.28390597); break;
		case 'f': value = (int)Math.round(6.452956326); break;
		case 'g': value = (int)Math.round(6.658995013); break;
		case 'h': value = (int)Math.round(10.55400296); break;
		case 'i': value = (int)Math.round(7.685352622); break;
		case 'j': value = (int)Math.round(2.529453669); break;
		case 'k': value = (int)Math.round(7.446983396); break;
		case 'l': value = (int)Math.round(5.46769686); break;
		case 'm': value = (int)Math.round(8.906789413); break;
		case 'n': value = (int)Math.round(6.018959943); break;
		case 'o': value = (int)Math.round(6.171297057); break;
		case 'p': value = (int)Math.round(8.645405228); break;
		case 'q': value = (int)Math.round(1.972710833); break;
		case 'r': value = (int)Math.round(6.719272289); break;
		case 's': value = (int)Math.round(8.85308784); break;
		case 't': value = (int)Math.round(5.340566606); break;
		case 'u': value = (int)Math.round(3.96350485); break;
		case 'v': value = (int)Math.round(3.704312565); break;
		case 'w': value = (int)Math.round(5.157542879); break;
		case 'x': value = (int)Math.round(2.757411365); break;
		case 'y': value = (int)Math.round(7.430544139); break;
		case 'z': value = (int)Math.round(4.16461176); break;
		}
	 */
}