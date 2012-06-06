/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seven.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import org.apache.log4j.Logger;
/**
 *
 * @author Satyajeet
 */
public class GameController {

	private static final int TIME_LIMIT = 1000;
	
    ArrayList<Boolean> isplayerdone;
    private Logger log = Logger.getLogger(GameController.class);

    private ArrayList<Integer> pointsSpent = new ArrayList<Integer>();
    
    private boolean newRound = true; // indicates if we're starting a new round
    
    // If gameover this function will return a (-1)
    public GameResult GamePlay(GameConfig gc_local)
    {
        isplayerdone = new ArrayList<Boolean>();
        GameResult gameresult = new GameResult(0, new ArrayList<Integer>());
        int retValue=0;

        //log.trace("Letters done: " + gc_local.num_leters_done);
        
    	// see if we're starting a new round
        if (newRound) {
        	//log.trace("NEW ROUND");
        	
        	// send each player its secretState
            for(int i=0;i<gc_local.PObjectList.size();i++)
            {
                Player currPlayer = gc_local.PObjectList.get(i);
                currPlayer.newRound(gc_local.secretstateList.get(i), gc_local.current_round);
            }
            
            newRound = false;
        }
        
        if(pointsSpent.size() == 0)
        {
        	for(int i=0;i<gc_local.PObjectList.size();i++)
        	{
        		pointsSpent.add(0);
        	}
        }
        if(gc_local.isMoreBiddingLeft() == true)
        {
            Letter bidLetter = gc_local.letterGame.getRandomFromBag();
            // The do the bidding stuff
            // Call the bid method for all players
            PlayerBids thisBid = new PlayerBids(bidLetter);
            for(int i=0;i<gc_local.PObjectList.size();i++)
            {
            	long start = System.currentTimeMillis();
                Player currPlayer = gc_local.PObjectList.get(i);
                //log.info("Requesting bid from player " + loop + " (" + currPlayer.getClass().getName() + ")");
                int bidValue = currPlayer.getBid((Letter) bidLetter.clone(),gc_local.BidList,gc_local.PlayerList,gc_local.secretstateList.get(i));
                // If player is full makes his value = 0.
                if(bidValue < 0)
                {
                    bidValue = 0;
                }
                log.trace("Player #" + i + " [" + currPlayer.getClass().getName() + "] bid " + bidValue + " for " + bidLetter);
                
//                if(isPlayerDone(loop, gc_local))
//                {
                    // The winner also needs to have a FALSE value for this field to get thru.
//                    bidValue = 0;
//                    isplayerdone.add(Boolean.TRUE);
//                }
//                else
//                {
                    isplayerdone.add(Boolean.FALSE);
//                }
                thisBid.bidvalues.add(bidValue);
                long t = System.currentTimeMillis() - start;
                if(t > TIME_LIMIT)
                {
                	log.error("Player " + i + " ("+ currPlayer.getClass().getName() + ") took too long: " + t + "ms");
                	// TODO: what happens if they take too long?
                }
            }
            // add this PlayerBid to the bidlist
            //log.info("Done collecting bids");
            thisBid.TargetLetter = bidLetter;
            gc_local.BidList.add(thisBid);
            // Confirm that the bids are ok and resolve auction
            if(isThisBidOk(gc_local) == true)
            {
                conductAuction(gc_local,bidLetter);
            }

            log.trace("The letter " + thisBid.TargetLetter + " was won by player #" + thisBid.winnerID + " and the amount paid was " + thisBid.winAmount);
            for(int i=0;i<gc_local.PObjectList.size();i++)
            {
                Player currPlayer = gc_local.PObjectList.get(i);
                boolean won = (thisBid.winnerID == i);
                currPlayer.bidResult(won, thisBid.TargetLetter, thisBid);
            }      

        }
        else
        {
        	
        	gc_local.lasPointsSpent = pointsSpent;
        	pointsSpent = new ArrayList<Integer>();
            // Before we do the clearance work, let us get the words
            gc_local.PlayerWords.clear();
            for(int loop=0;loop<gc_local.PObjectList.size();loop++)
            {
                Player currP = gc_local.PObjectList.get(loop);
            	long start = System.currentTimeMillis();
                String word = currP.getWord();
                long end = System.currentTimeMillis();
                if (end - start > TIME_LIMIT) 
                	log.error("Player #" + loop + " took too long to return a word!");
                gc_local.PlayerWords.add(word);
            }
            // Validate and change scores:
            validateAndScore(gc_local);

        	/*
        	 * This is the initialization for a new round
        	 */
            
            // Get the word from each, calculate score, store words in history
            // Also make a new wordbag
            gc_local.letterGame.initBag();

            gc_local.num_leters_done = gc_local.number_of_secret_objects*gc_local.PlayerList.size();
            // Now first clear the open letters
            for(int loop=0;loop<gc_local.PlayerList.size();loop++)
            {
                OpenState currOS = gc_local.openstateList.get(loop);
                currOS.openLetters.clear();
                SecretState currSS = gc_local.secretstateList.get(loop);
                currSS.secretLetters.clear();
            }


            // Now let us fill it with new letters from the random bag

            for(int loop=0;loop<gc_local.PlayerList.size();loop++)
            {
            SecretState currSS = gc_local.secretstateList.get(loop);
            for(int wordloop =0;wordloop<gc_local.number_of_secret_objects;wordloop++)
                {
                Letter temp_letter = gc_local.letterGame.getRandomFromBag();
                currSS.secretLetters.add(temp_letter);

                }

            }
            ArrayList<Integer> finalScores = new ArrayList<Integer>();
            for(int loop=0;loop<gc_local.PObjectList.size();loop++)
            {
            	finalScores.add(gc_local.secretstateList.get(loop).getScore());
            }
            for(int loop=0;loop<gc_local.PObjectList.size();loop++)
            {
                Player currP = gc_local.PObjectList.get(loop);
                currP.updateScores((ArrayList<Integer>) finalScores.clone());
            }


            gc_local.current_round++; // we count only words returned as rounds, bidding = step
            newRound = true;
        }



        // Termination logic
        // This is at the end after program logic
        if(gc_local.current_round == gc_local.number_of_rounds)
        {
            retValue = -1;
            gameresult.retValue = -1;
            for(int loop=0;loop<gc_local.PlayerList.size();loop++)
            {
                int score = gc_local.secretstateList.get(loop).score;
                gameresult.scoreList.add(score);
            }
        }

       return gameresult;
    }


    public boolean isPlayerDone(int playedId, GameConfig gc_local)
    {
        boolean result = true;
        int openLetters = gc_local.openstateList.get(playedId).openLetters.size();
        int secretLetters = gc_local.number_of_secret_objects;
        if(openLetters + secretLetters < 7)
        {
            result = false;
        }

        return result;
    }

    public void validateAndScore(GameConfig gc_local)
    {
        gc_local.wordbag.clear();
        gc_local.lasPoints.clear();
        
        // For every word in the list, validate and score
        for(int loop=0;loop<gc_local.PlayerWords.size();loop++)
        {
            String playerword = gc_local.PlayerWords.get(loop);
            //log.trace("Word is " + playerword);
            // First see if it contains correct letters from its stack.
            ArrayList<Character> availableLetters = new ArrayList<Character>();
            // Lets fill this from secret and open letters
            for(int loop2=0;loop2<gc_local.openstateList.get(loop).openLetters.size();loop2++)
            {
                Character temp_char = new Character(gc_local.openstateList.get(loop).openLetters.get(loop2).character);
                availableLetters.add(temp_char);
            }
            for(int loop2=0;loop2<gc_local.secretstateList.get(loop).secretLetters.size();loop2++)
            {
                Character temp_char = new Character(gc_local.secretstateList.get(loop).secretLetters.get(loop2).character);
                availableLetters.add(temp_char);
            }
            // Create a string out of them, and add to wordbag
            String wordbagString = " ";
            for(int loopx=0;loopx<availableLetters.size();loopx++)
            {
                wordbagString += availableLetters.get(loopx).toString() + " ";
            }
            gc_local.wordbag.add(wordbagString);

            //Now we start removing all of them
            Boolean wordOK = true;
            for(int loop2=0;loop2<playerword.length();loop2++)
            {
                Character currCh = playerword.charAt(loop2);
                if(availableLetters.contains(currCh))
                {
                    availableLetters.remove(currCh);
                }
                else
                {
                    wordOK = false;
                    log.error("Invalid word by Player: " + gc_local.PlayerList.get(loop));
                    break;
                }
            }
            if(wordOK && LetterGame.getWordScore(playerword) != -1)
            {
                // Award them the points
                int Currscore = LetterGame.getWordScore(playerword);
                if(playerword.length() == 7)
                {
                    Currscore = Currscore + 50;
                }
                //Now add this to his secretstate
                gc_local.secretstateList.get(loop).score += Currscore;
                gc_local.lasPoints.add(Currscore);

            }
            else
            {
                gc_local.lasPoints.add(0);
            }


        }

    }

    public void conductAuction(GameConfig gc_local, Letter bidletter)
    {
        int lastbidIndex = gc_local.BidList.size()-1;
        PlayerBids lastBid = gc_local.BidList.get(lastbidIndex);
        int winnerIndex,runnerUpIndex; // Let us get someone for this
        winnerIndex = 0;
        runnerUpIndex = 0;


        // Create an arraylist of bid,index
        ArrayList<Bidval> bidvalarr = new ArrayList<Bidval>();
        for(int loop=0;loop<lastBid.bidvalues.size();loop++)
        {
            Bidval temp_bv = new Bidval(lastBid.bidvalues.get(loop),loop);
            bidvalarr.add(temp_bv);
        }
        Collections.sort(bidvalarr,new BidvalComparator());
        for(Bidval bv: bidvalarr){
        	//System.out.println(bv.bid);
        }

        int gotCount = 1;
        int winnerLoopIndex = 0;
        for(int loop=0;loop<bidvalarr.size();loop++)
        {
            if(isplayerdone.get(bidvalarr.get(loop).index) == false && gotCount == 1)
            {
                gotCount++;
                winnerIndex = bidvalarr.get(loop).index;
                winnerLoopIndex = loop;
                break;
            }
//            else if(gotCount == 2)
//            {
//                runnerUpIndex = bidvalarr.get(loop).index;
//                gotCount++;
//            }
        }

//        if(gotCount == 2)
//        {
        	runnerUpIndex = winnerIndex;
             for(int loop=winnerLoopIndex+ 1;loop<bidvalarr.size();loop++)
            {
                if(isplayerdone.get(bidvalarr.get(loop).index) == false)
                {
                    runnerUpIndex = bidvalarr.get(loop).index;
                    break;
                }
             }
//       }

        // Now we know that winnerIndex has max values, but there could be others with the same bid.
        // Lets load them up in a arraylist
        ArrayList<Integer> winnerList = new ArrayList<Integer>();

        for(int loop=0;loop<lastBid.bidvalues.size();loop++)
        {
            if(lastBid.bidvalues.get(loop) == lastBid.bidvalues.get(winnerIndex) && isplayerdone.get(loop) == false)
            {
                winnerList.add(loop);
            }
        }

        if(winnerList.size() > 1)
        {
            // This means we need to take a random person from here as the winner
            Random rand = new Random();
            winnerIndex = winnerList.get(rand.nextInt(winnerList.size()));
        }

        lastBid.winAmount = lastBid.bidvalues.get(winnerIndex);
        lastBid.wonBy = gc_local.PlayerList.get(winnerIndex);
        lastBid.winnerID = winnerIndex;
        lastBid.winAmount = lastBid.bidvalues.get(runnerUpIndex);

        pointsSpent.set(winnerIndex, pointsSpent.get(winnerIndex) + lastBid.winAmount);
        
        SecretState winnerSS = gc_local.secretstateList.get(winnerIndex);
        int secretCount = winnerSS.secretLetters.size();
        OpenState winnerOS = gc_local.openstateList.get(winnerIndex);
        int openCount = winnerOS.openLetters.size();
        int winnerTotalLetter = secretCount + openCount;
        // Award this letter to him
        winnerOS.openLetters.add(bidletter);
        lastBid.TargetLetter = bidletter;
        // Also reduce his score/money
        winnerSS.score -= lastBid.bidvalues.get(runnerUpIndex);
        gc_local.num_leters_done++;
    }


    public boolean isThisBidOk(GameConfig gc_local)
    {
        // Ensure that no one bid above their scores.
        boolean result = true;
        int lastbidIndex = gc_local.BidList.size()-1;
        PlayerBids lastBid = gc_local.BidList.get(lastbidIndex);
        for(int loop=0;loop<lastBid.bidvalues.size();loop++)
        {
            SecretState thisSS = gc_local.secretstateList.get(loop);
            int score = thisSS.score;

            int bidvalue = lastBid.bidvalues.get(loop);

            if(bidvalue > score )
            {
                bidvalue = score; // Instead of removing him from the list, we change his bid to be = to the money he has left.

                lastBid.bidvalues.remove(loop);
                lastBid.bidvalues.add(loop, score);



                // This is not valid. Drop this player from
//                gc_local.BidList.remove(loop);
//                gc_local.PObjectList.remove(loop);
//                gc_local.PlayerList.remove(loop);
//                gc_local.openstateList.remove(loop);
//                gc_local.secretstateList.remove(loop);
//                result = false;
//                break;
            }
        }

        return result;
    }

}

class Bidval{
    int bid;
    int index;

    public Bidval(int b, int i)
    {
        bid = b;
        index = i;
    }
}

class BidvalComparator implements Comparator
{

    public int compare(Object o1, Object o2) {

        int bid1 = ((Bidval)o1).bid;
        int bid2 = ((Bidval)o2).bid;

        if(bid2>bid1)
        {
            return 1;
        }
        else if(bid2<bid1)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

}