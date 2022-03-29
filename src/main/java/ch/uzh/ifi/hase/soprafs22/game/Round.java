package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.Card;

import java.util.ArrayList;

public class Round {

    private BlackCard blackCard;
    private ArrayList<User> players;
    private Card chosenCard;

    public void startNewRound(){}

    public void setRoundPlayers(){}

    public void setBlackCard(){}

    public void getBlackCard(){}

    public void updateHands(){}

    public void setChosenCard(long cardId, User owner){}

    public ArrayList<Card> getAllChosenCards(){
        //just example, so something gets returned
        return new ArrayList<Card>();
    }

    public User getRoundWinner(){
        //just example, so something gets returned
        return new User();
    }

}
