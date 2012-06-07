package seven.g1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import seven.ui.Letter;
import seven.ui.SecretState;

public class Statistics
{
	public Map<Character, LetterObject> available = new HashMap<Character, LetterObject>();
	//public Map<Character>
	
	//public Map<Integer, Opponents> oponnents
	
	public List<Word> availableWords;
	
	public List<Character> chars;
	
	public Statistics(SecretState state, List<Word> words)
	{
		chars = new ArrayList<Character>();
		for (Letter l : state.getSecretLetters())
		{
			chars.add(Character.toLowerCase(l.getCharacter()));
		}this.availableWords = words;
		available.put('a', new LetterObject('a', 1, 9));
		available.put('b', new LetterObject('b', 3,2));
		available.put('c', new LetterObject('c', 3,2));
		available.put('d', new LetterObject('d', 2,4));
		available.put('e', new LetterObject('e', 1,12));
		available.put('f', new LetterObject('f', 4,2));
		available.put('g', new LetterObject('g', 2,3));
		available.put('h', new LetterObject('h', 4,2));
		available.put('i', new LetterObject('i', 1,9));
		available.put('j', new LetterObject('j', 8,1));
		available.put('k', new LetterObject('k', 5,1));
		available.put('l', new LetterObject('l', 1,4));
		available.put('m', new LetterObject('m', 3,2));
		available.put('n', new LetterObject('n', 1,6));
		available.put('o', new LetterObject('o', 1,8));
		available.put('p', new LetterObject('p', 3,2));
		available.put('q', new LetterObject('q', 10,1));
		available.put('r', new LetterObject('r', 1,6));
		available.put('s', new LetterObject('s', 1,4));
		available.put('t', new LetterObject('t', 1,6));
		available.put('u', new LetterObject('u', 1,4));
		available.put('v', new LetterObject('v', 4,2));
		available.put('w', new LetterObject('w', 4,2));
		available.put('x', new LetterObject('x', 8,1));
		available.put('y', new LetterObject('y', 4,2));
		available.put('z', new LetterObject('z', 10,1));
		updateCharStats('0');
		
		for (Letter l : state.getSecretLetters())
		{
			removeChar(l.getCharacter());
		}
	}
	
	public void removeChar(Character c)
	{
		LetterObject o = available.get(Character.toLowerCase(c));
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
			Word w = new Word(b.toString().toUpperCase());
			
			// Remove all words that no longer apply.
			Iterator<Word> words = availableWords.iterator();
			while (words.hasNext())
			{
				Word word = words.next();
				if (!word.contains(w))
				{
					words.remove();
				}
			}
		}
		// Get the count of words that are lost.
		for (Character chars : available.keySet())
		{
			b.append(chars);
			LetterObject o = available.get(Character.toLowerCase(chars));
			o.setStats(0);
			Word w = new Word(b.toString().toUpperCase());
			for (Word word : availableWords)
			{
				if (!word.contains(w))
				{
					o.setStats(o.getStats()+1);
				}
			}
		}
		// create a percentage for the results.
		for (Character chars : available.keySet())
		{
			LetterObject o = available.get(Character.toLowerCase(chars));
			o.setStats(o.getStats()/availableWords.size());
		}
		
	}
	
	public double getStatistics(char c)
	{
		return available.get(Character.toLowerCase(c)).getStats();
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