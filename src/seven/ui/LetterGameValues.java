package seven.ui;

public final class LetterGameValues {
	
    public static int getWordScore(String word)
    {
    	/* assume word is in dictionary */
    	if(word.length() > 7) {
    		return 0;
    	}
        // Lets compute the score.
        int score = 0;
        if(word.length() == 7) {
        	score += 50;
        }
        
        for(int loop=0;loop<word.length();loop++)
        {
        	Character currChar = word.charAt(loop);
        	score += letterScore(currChar);
        }
        
        return score;
    }

	public static int letterScore(Character letter)
    {
        int score = 0;
        switch(letter)
        {
            case 'E': score = 1;break;
            case 'A': score = 1;break;
            case 'I': score = 1;break;
            case 'O': score = 1;break;
            case 'N': score = 1;break;
            case 'R': score = 1;break;
            case 'T': score = 1;break;
            case 'L': score = 1;break;
            case 'S': score = 1;break;
            case 'U': score = 1;break;
            case 'D': score = 2;break;
            case 'G': score = 2;break;

            case 'B': score = 3;break;
            case 'C': score = 3;break;
            case 'M': score = 3;break;
            case 'P': score = 3;break;

            case 'F': score = 4;break;
            case 'H': score = 4;break;
            case 'V': score = 4;break;
            case 'W': score = 4;break;
            case 'Y': score = 4;break;

            case 'K': score = 5;break;
            case 'J': score = 8;break;
            case 'X': score = 8;break;
            case 'Q': score = 10;break;
            case 'Z': score = 10;break;
            case '*': score = 0;break;

            default:
            	score = 0;
            	break;
        }

        return score;
    }
	
	public static int getLetterFrequency(Character letter)
	{
        int freq = 0;
        switch(letter)
        {
            case 'E': freq = 12;break;
            case 'A': freq = 9;break;
            case 'I': freq = 9;break;
            case 'O': freq = 8;break;
            case 'N': freq = 6;break;
            case 'R': freq = 6;break;
            case 'T': freq = 6;break;
            case 'L': freq = 4;break;
            case 'S': freq = 4;break;
            case 'U': freq = 4;break;
            case 'D': freq = 4;break;
            case 'G': freq = 3;break;

            case 'B': freq = 2;break;
            case 'C': freq = 2;break;
            case 'M': freq = 2;break;
            case 'P': freq = 2;break;

            case 'F': freq = 2;break;
            case 'H': freq = 2;break;
            case 'V': freq = 2;break;
            case 'W': freq = 2;break;
            case 'Y': freq = 2;break;

            case 'K': freq = 1;break;
            case 'J': freq = 1;break;
            case 'X': freq = 1;break;
            case 'Q': freq = 1;break;
            case 'Z': freq = 1;break;
            case '*': freq = 0;break;
            
            default:
            	freq = 0;
            	break;

        }

        return freq;
    }
	
}
