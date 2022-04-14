package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.helpers.GameStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Countdown;

import java.util.ArrayList;

public class Match {
    private ArrayList<User> players = new ArrayList<>();
    private GameStatus gameStatus;
    private ArrayList<Hand> allPlayersHands;

    //not explicitly in UML, but needed
    private Lobby lobby;
    private ScoreBoard scoreBoard;
    private Countdown timer;
    private Round round;

    private static Match match = null;
    public static Match getInstance(){
        if (match == null){
            match = new Match();
        }
        return match;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus(){
        return gameStatus;
    }

    //check if array or array list
    public void createDeck(){
    }

    public void createHands(){
        for (User player: players) {
            Hand hand = new Hand(player);
            hand.createHand(); //Creating a hand with 10 cards
            allPlayersHands.add(hand);
        }
    }

    public int getRound(){
        return 0;
    }

    public ArrayList<User> getGamePlayers(){
        return players;
    }

    public void setGamePlayers(ArrayList<User> users){
        this.players = users;
    }



}