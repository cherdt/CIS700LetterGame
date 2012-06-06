/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seven.ui;

import java.util.ArrayList;

/**
 *
 * @author Satyajeet
 */
public class PlayerBids {

    ArrayList<Integer> bidvalues; // players from 0 to n-1
    Letter TargetLetter;
    String wonBy;
    int winnerID;
    int winAmount;
    public PlayerBids(Letter targetletter) {

        bidvalues = new ArrayList<Integer>();
        TargetLetter = targetletter;
    }
	/**
	 * @return the bidvalues
	 */
	public ArrayList<Integer> getBidvalues() {
		return bidvalues;
	}
	/**
	 * @return the targetLetter
	 */
	public Letter getTargetLetter() {
		return TargetLetter;
	}
	/**
	 * @return the wonBy
	 */
	public String getWonBy() {
		return wonBy;
	}
	/**
	 * @return the winAmmount
	 */
	public int getWinAmmount() {
		return winAmount;
	}
	/**
	 * @return the winnerID
	 */
	public int getWinnerID() {
		return winnerID;
	}
    


}
