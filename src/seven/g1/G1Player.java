package seven.g1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import seven.g1.bean.Word;
import seven.ui.Player;

public abstract class G1Player implements Player {

	// an array of words to be used for making decisions
	protected static final Word[] wordlist;
	protected static final Word[] sevenLetterWords;
	
	/* This code initializes the word list */
	static {
		BufferedReader r;
		String line = null;
		ArrayList<Word> wtmp = new ArrayList<Word>(55000);
		ArrayList<Word> seven = new ArrayList<Word>(27000);
		try {
			// you can use textFiles/dictionary.txt if you want the whole list
			// or you can use super-small-wordlist.txt
			r = new BufferedReader(new FileReader("textFiles/dictionary.txt"));
			while (null != (line = r.readLine())) {

				String[] split = line.split(","); 
				if ( split.length==2 )
				{
					wtmp.add(new Word(split[1].trim()));
					// Add 7-letter words to a special list
					if ( split[1].trim().length() == 7 ) {
						seven.add(new Word(split[1].trim()));
					}
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
	
	public static int getBestScore(List<Character> letters) {
		
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
		
		return bestword.score;
	}
	
}
