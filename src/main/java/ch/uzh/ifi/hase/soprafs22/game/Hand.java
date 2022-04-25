package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;

import java.util.ArrayList;

//Not yet sure if Hand should be in game
public class Hand {

    private ArrayList<WhiteCard> hand = new ArrayList<WhiteCard>();
    private WhiteCard chosenCard;
    private User owner;

    public Hand(User owner) {
        this.owner = owner;
    }

    //should draw ten white cards from the deck and save it in the hand
    public void createHand(){
        int numberOfCards = 0;
        while (numberOfCards < 10){
            WhiteCard newCard = new WhiteCard(owner);
            newCard.createCard(); //setting owner and text for the card
            boolean cardWithSameText = false;
            for (WhiteCard card: hand){
                if(newCard.getText() == card.getText()){
                    cardWithSameText = true;
                    break;
                }
            }
            if (!cardWithSameText){
                hand.add(newCard);
                numberOfCards++;
            }
        }
    }

    public User getOwner(){
        return this.owner;
    }

    public void addingCard(){}
}
