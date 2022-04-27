
package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.helpers.GameStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Countdown;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;

/**
 * A match contains several rounds
 * */

public class Match {
    private Long id;
    private ArrayList<User> players = new ArrayList<>();
    private ArrayList<Hand> allPlayersHands = new ArrayList<>();

    private ScoreBoard scoreBoard;
    private Timer timer;
    private Round round = new Round(allPlayersHands);

    public Match(Long id) {
        this.id = id;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public Timer getTimer() {
        return timer;
    }

    public Round getRound(){
        return round;
    }

    public ArrayList<User> getMatchPlayers(){
        return players;
    }

    public void setMatchPlayers(ArrayList<User> users){
        this.players = users;
    }

    public Long getId() {
        return id;
    }

    // creating a hand with 10 cards
    public void createHands(){
        for (User player: players) {
            Hand hand = new Hand(player);
            hand.createHand();
            allPlayersHands.add(hand);
        }
    }

    // add a new card to the players' hand
    public void updateHands(){
        // for each hand, add a new card after each round since players already played with it
        for (Hand hand: allPlayersHands){
            if(round.isRoundFinished){
                hand.updateHand();
            }
        }
    }

    public Hand getHandByUserId(Long userId){
        String errorMsg = "Hand not found";

        for (Hand hand: allPlayersHands){
            if (Objects.equals(hand.getOwner().getId(), userId)){
                return hand;
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);

    }

}