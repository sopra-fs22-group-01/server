package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.helpers.GameStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Countdown;

import java.util.ArrayList;

public class Match {
    private Long id;
    private ArrayList<User> players = new ArrayList<>();
    private ArrayList<Hand> allPlayersHands;

    private ScoreBoard scoreBoard;
    private Countdown timer;
    private Round round;

    public Match(Long id) {
        this.id = id;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public Countdown getTimer() {
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

    public void createHands(){
        for (User player: players) {
            Hand hand = new Hand(player);
            hand.createHand(); //Creating a hand with 10 cards
            allPlayersHands.add(hand);
        }
    }

}