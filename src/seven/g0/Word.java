package seven.g0;

import seven.ui.LetterGame;

public class Word {

	private static final int LETTERS = 26;
	public String word;
	public int length;
	public int score = 0;
	//countkeep implementation: the value is the frequency of the letter in the word, and the index is the letter ex A is 0
	public int[] countKeep= new int[LETTERS];

	public Word(String s){
		word=s;
		length=s.length();
		for(int i = 0; i<s.length();i++){
			char c = s.charAt(i);
			score += LetterGame.letterScore(c);
			int index= Integer.valueOf(c);
			index -= Integer.valueOf('A');
			countKeep[index]++;
		}
	}

	public Word(final int[] counts) {
		countKeep = counts;
		length = 0;
		StringBuffer b = new StringBuffer();
		int charOffset = Integer.valueOf('A');
		for (char c = 'A'; c <= 'Z'; c++) {
			int index= Integer.valueOf(c) - charOffset;
			for (int i = 0; i < countKeep[index]; i++) {
				score += LetterGame.letterScore(c);
				b.append(c);
				length++;
			}
		}
		word = b.toString();
		assert(length == word.length());
	}
	/**
	 * returns true if the word w can be formed from the letters contained in the word bag object we have currently
	 * @return
	 */
	public boolean contains(Word w){

		for (int i=0;i<LETTERS;i++){
			if(this.countKeep[i]<w.countKeep[i])
				return false;
		}
		return true;

	}


}
