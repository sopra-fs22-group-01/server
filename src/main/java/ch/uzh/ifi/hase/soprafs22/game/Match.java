package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.helpers.GameStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Timer;

import java.util.ArrayList;

public class Match {
    private Long id;
    private ArrayList<User> players = new ArrayList<>();
    private ArrayList<Hand> allPlayersHands;

    private ScoreBoard scoreBoard;
    private Timer timer;
    private Round round;

    public Match(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void createHands(){
        for (User player: players) {
            Hand hand = new Hand(player);
            hand.createHand(); //Creating a hand with 10 cards
            allPlayersHands.add(hand);
        }
    }

    public Round getRound(){
        return round;
    }

    public ArrayList<User> getGamePlayers(){
        return players;
    }

    public void setGamePlayers(ArrayList<User> users){
        this.players = users;
    }


    //returns a copy of the current scoreboard
    public ScoreBoard getScoreBoard(){
        ScoreBoard scoreBoardCopy = new ScoreBoard();
        scoreBoardCopy = this.scoreBoard;
        return scoreBoardCopy;
    }
}