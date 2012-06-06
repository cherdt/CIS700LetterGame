package seven.ui;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.apache.log4j.Logger;

/**
 *
 * @author Satyajeet
 */
public class LetterGame {

	public static final String DICTIONARY_FILE = "textFiles/dictionary.txt";
	
     static final Dictionary dictionary = new Dictionary();
     static ArrayList<Letter> wordbag;
     static {
    	 initDict();
     }
     public LetterGame()
     {
         initBag();
     }
     
     public static Set<String> getDictionaryWords() {
    	 return dictionary.wordlist.keySet();
     }
     
     public static ArrayList<Letter> getRemainingLetters() {
    	 ArrayList<Letter> letters = new ArrayList<Letter>();
    	 Collections.copy(letters, wordbag);
    	 return letters;
     }

  	private static Logger log = Logger.getLogger(LetterGame.class);

  	public static int getWordScore(String word)
     {
         int score = -1;


         
         if(dictionary.wordlist.containsKey(word))
         {
             if(dictionary.wordlist.get(word))
             {
                 // Lets compute the score.
                 score = 0;
                 for(int loop=0;loop<word.length();loop++)
                 {
                     Character currChar = word.charAt(loop);
                     score += letterScore(currChar);
                 }

             }
         }
         else log.error("dictionary.wordlist doesn't contain key " + word);
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
             default: throw new IllegalArgumentException("no score for " + letter);

         }

         return score;
     }

     public Letter getRandomFromBag()
     {
         Letter letter = null;

         Random rand = new Random();
         if(wordbag.size() <= 0)
        	 System.err.println("Word bag size: " + wordbag.size());
         int index = rand.nextInt(wordbag.size()); // This accounts for zero indexing

         letter = wordbag.remove(index);

         return letter;
     }

     public void initBag()
     {
         wordbag = new ArrayList<Letter>();
         // * = blank word
//         wordbag.add(new Letter('*', 0));
//         wordbag.add(new Letter('*', 0));

         for(int loop = 1;loop<=12;loop++)
         {
             wordbag.add(new Letter('E', 1));
         }
         for(int loop = 1;loop<=9;loop++)
         {
             wordbag.add(new Letter('A', 1));
         }
         for(int loop = 1;loop<=9;loop++)
         {
             wordbag.add(new Letter('I', 1));
         }
         for(int loop = 1;loop<=8;loop++)
         {
             wordbag.add(new Letter('O', 1));
         }
         for(int loop = 1;loop<=6;loop++)
         {
             wordbag.add(new Letter('N', 1));
         }
         for(int loop = 1;loop<=6;loop++)
         {
             wordbag.add(new Letter('R', 1));
         }
         for(int loop = 1;loop<=6;loop++)
         {
             wordbag.add(new Letter('T', 1));
         }
         for(int loop = 1;loop<=4;loop++)
         {
             wordbag.add(new Letter('L', 1));
         }
         for(int loop = 1;loop<=4;loop++)
         {
             wordbag.add(new Letter('S', 1));
         }
         for(int loop = 1;loop<=4;loop++)
         {
             wordbag.add(new Letter('U', 1));
         }

         for(int loop = 1;loop<=4;loop++)
         {
             wordbag.add(new Letter('D', 2));
         }
         for(int loop = 1;loop<=3;loop++)
         {
             wordbag.add(new Letter('G', 2));
         }

         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('B', 3));
         }
         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('C', 3));
         }
         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('M', 3));
         }
         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('P', 3));
         }


         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('F', 4));
         }
         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('H', 4));
         }
         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('V', 4));
         }
         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('W', 4));
         }
         for(int loop = 1;loop<=2;loop++)
         {
             wordbag.add(new Letter('Y', 4));
         }

         wordbag.add(new Letter('K', 5));
         wordbag.add(new Letter('J', 8));
         wordbag.add(new Letter('X', 8));
         wordbag.add(new Letter('Q', 10));
         wordbag.add(new Letter('Z', 10));





     }

    private static void initDict()
    {
    	//if (null != dictionary) return;
        try{
            CSVReader csvreader = new CSVReader(new FileReader(DICTIONARY_FILE));
            String[] nextLine;
            csvreader.readNext(); // Waste the first line
            while((nextLine = csvreader.readNext()) != null)
            {
            	if (nextLine.length > 1)  {
            		String word = nextLine[1];
            		dictionary.wordlist.put(word, Boolean.TRUE);
            	}
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("\n Could not load dictionary!");
        }

    }

}

class Dictionary
{
    Hashtable<String,Boolean> wordlist;

    public Dictionary()
    {
        wordlist = new Hashtable<String, Boolean>();
    }


}
