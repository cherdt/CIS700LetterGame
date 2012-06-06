/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seven.ui;

import java.io.FileWriter;
import java.util.ArrayList;

/**
 * 
 * @author Satyajeet
 */
public class GameConfig {

	ArrayList<OpenState> openstateList;
	ArrayList<SecretState> secretstateList;
	ArrayList<PlayerBids> BidList;
	int number_of_secret_objects; // from 0 to 7
	ArrayList<String> PlayerList;
	ArrayList<Player> PObjectList;
	ArrayList<String> PlayerWords;
	ArrayList<Integer> lasPoints;
	ArrayList<Integer> lasPointsSpent;
	LetterGame letterGame;

	int gameDelay = 500;
	int number_of_rounds;
	int current_round;
	public int num_leters_done;
	public FileWriter outFile = null;
	ArrayList<String> wordbag;

	public String getPlayersPlaying()
	{
		String ret = "";
		for(String s : PlayerList)
		{
			ret += s.replace("seven.", "")+" ";
		}
		return ret;
	}
	public GameConfig(ArrayList<String> plist, int secret_objs,
			IOController ioc, int numRounds) {

		// This does the basic shell memory allocations, actual data is handled
		// by game controller.
		wordbag = new ArrayList<String>();
		letterGame = new LetterGame();
		BidList = new ArrayList<PlayerBids>();
		PlayerWords = new ArrayList<String>();
		lasPointsSpent = new ArrayList<Integer>();
		PObjectList = new ArrayList<Player>();
		lasPoints = new ArrayList<Integer>();
		PlayerList = plist;
		number_of_secret_objects = secret_objs;
		current_round = 0;
		number_of_rounds = numRounds;
		num_leters_done = secret_objs*plist.size();
		
		for (int loop = 0; loop < PlayerList.size(); loop++) {
			Player ptemp = ioc.getPObject(PlayerList.get(loop));

			ptemp.newGame(loop, number_of_rounds, PlayerList.size());
			PObjectList.add(ptemp);
		}

		// Now you also need to init the player state
		openstateList = new ArrayList<OpenState>();
		secretstateList = new ArrayList<SecretState>();
		// Also put player number of states in it
		for (int loop = 0; loop < PlayerList.size(); loop++) {
			OpenState temp_os = new OpenState();
			openstateList.add(temp_os);

			SecretState temp_ss = new SecretState(100); // You start with 100
														// points
			secretstateList.add(temp_ss);
		}

		// Assign the first set of secret letters
		for (int loop = 0; loop < PlayerList.size(); loop++) {
			SecretState currSS = secretstateList.get(loop);
			for (int wordloop = 0; wordloop < number_of_secret_objects; wordloop++) {
				Letter temp_letter = letterGame.getRandomFromBag();
				currSS.secretLetters.add(temp_letter);
			}
		}

	}

	public boolean isMoreBiddingLeft() {
		
		return num_leters_done < 8*PlayerList.size();
	}

}
