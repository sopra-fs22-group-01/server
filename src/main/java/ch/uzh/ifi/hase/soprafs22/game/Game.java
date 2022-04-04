package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.deck.BlackDeck;
import ch.uzh.ifi.hase.soprafs22.game.deck.WhiteDeck;
import ch.uzh.ifi.hase.soprafs22.game.helpers.GameStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Timer;

import java.util.ArrayList;

public class Game{
    private ArrayList<User> players = new ArrayList<User>();
    private GameStatus gameStatus;
    private BlackDeck blackDeck;
    private WhiteDeck whiteDeck;
    private ArrayList<Hand> allPlayersHands;

    //not explicitly in UML, but needed
    private Lobby lobby;
    private ScoreBoard scoreBoard;
    private Timer timer;
    private Round round;

    public Game getInstance(){
        //just some implementation so the method returns of type game
        //should we really use singleton?
        Game game = new Game();
        return game;
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


}