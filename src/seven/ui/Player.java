package seven.ui;

import java.util.ArrayList;

/**
 *
 * @author Chris Murphy
 */
public interface Player {

    public void newGame(int id, int number_of_rounds, int number_of_players);
    
    public void newRound(SecretState secretState, int current_round);

    public int getBid(Letter bidLetter, ArrayList<PlayerBids> PlayerBidList, ArrayList<String> PlayerList, SecretState secretstate);
    
    public void bidResult(boolean won, Letter letter, PlayerBids bids);

    public String getWord();
    
    public void updateScores(ArrayList<Integer> scores);
    
}

