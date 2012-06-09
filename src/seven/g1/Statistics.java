package seven.g1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.SecretState;

public class Statistics
{
	public Map<Character, LetterObject> available = new HashMap<Character, LetterObject>();
	//public Map<Character>
	
	//public Map<Integer, Opponents> oponnents
	
	public List<Word> availableWords;
	private Logger logger = Logger.getLogger(this.getClass());
	public List<Character> chars;
	
	public Statistics(SecretState state, List<Word> words)
	{
		chars = new ArrayList<Character>();
		for (Letter l : state.getSecretLetters())
		{
			chars.add(l.getCharacter());
		}this.availableWords = words;
		available.put('A', new LetterObject('A', 1, 9));
		available.put('B', new LetterObject('B', 3,2));
		available.put('C', new LetterObject('C', 3,2));
		available.put('D', new LetterObject('D', 2,4));
		available.put('E', new LetterObject('E', 1,12));
		available.put('F', new LetterObject('F', 4,2));
		available.put('G', new LetterObject('G', 2,3));
		available.put('H', new LetterObject('H', 4,2));
		available.put('I', new LetterObject('I', 1,9));
		available.put('J', new LetterObject('J', 8,1));
		available.put('K', new LetterObject('K', 5,1));
		available.put('L', new LetterObject('L', 1,4));
		available.put('M', new LetterObject('M', 3,2));
		available.put('N', new LetterObject('N', 1,6));
		available.put('O', new LetterObject('O', 1,8));
		available.put('P', new LetterObject('P', 3,2));
		available.put('Q', new LetterObject('Q', 10,1));
		available.put('R', new LetterObject('R', 1,6));
		available.put('S', new LetterObject('S', 1,4));
		available.put('T', new LetterObject('T', 1,6));
		available.put('U', new LetterObject('U', 1,4));
		available.put('V', new LetterObject('V', 4,2));
		available.put('W', new LetterObject('W', 4,2));
		available.put('X', new LetterObject('X', 8,1));
		available.put('Y', new LetterObject('Y', 4,2));
		available.put('Z', new LetterObject('Z', 10,1));
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
			b.append(ch);
		}
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
				if (!word.contains(w) || !stillAvailLetters.contains(word) )
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
				if (!word.contains(w))
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
			if ( availableWords.size() == 0 ) {
				logger.trace("Char " + o.getCharacter() + " loses 100%");
				o.setStats(1);
			} else {
				o.setStats( o.getStats()/ (double) availableWords.size());
			}
			
			logger.trace(o.getCharacter() + ":" + o.getStats());
			
		}
		
		
	}
	
	public double getStatistics(char c)
	{
		return available.get(c).getStats();
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