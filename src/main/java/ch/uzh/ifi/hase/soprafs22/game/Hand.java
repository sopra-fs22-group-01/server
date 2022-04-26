package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.Card;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

//Not yet sure if Hand should be in game//TEST
public class Hand {

    private ArrayList<WhiteCard> userHand = new ArrayList<WhiteCard>();
    private WhiteCard chosenCard;
    private User owner;

    public ArrayList<WhiteCard> getUserHand(){
        return this.userHand;
    }
    public void setUserHand(ArrayList<WhiteCard> hand){
        this.userHand = hand;
    }

    public WhiteCard getChosenCard(){return this.chosenCard;}
    public void setChosenCard(WhiteCard card){this.chosenCard = card;}

    public User getOwner(){return this.owner;}
    public void setOwner(User user){this.owner = user;} // test}

    public Hand(User owner) {
        this.chosenCard = new WhiteCard(owner);
        this.owner = owner;
    }

    //should draw ten white cards from the deck and save it in the hand
    public void createHand(){
        int numberOfCards = 0;
        while (numberOfCards < 10){
            WhiteCard newCard = new WhiteCard(this.owner);
            newCard.createCard(); //setting owner and text for the card
            boolean cardWithSameText = false;
            for (WhiteCard card: userHand){
                if(newCard.getText() == card.getText()){
                    cardWithSameText = true;
                    break;
                }
            }
            if (!cardWithSameText){
                userHand.add(newCard);
                numberOfCards++;
            }
        }
    }

}
