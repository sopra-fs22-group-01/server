package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;

import java.util.ArrayList;

//Not yet sure if Hand should be in game
public class Hand {

    private ArrayList<WhiteCard> hand = new ArrayList<WhiteCard>();
    private WhiteCard chosenCard;

    //should draw ten white cards from the deck and save it in the hand
    public Hand createHand(){return new Hand();}

    public void addingCard(){}
}
