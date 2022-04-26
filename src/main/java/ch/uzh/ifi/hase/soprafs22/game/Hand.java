package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.Card;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
//nevio du hesch nid rächt. oder doch?
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

    public void addingCard(){}


    public void setChosenCard(WhiteCard card){
        this.chosenCard = card;
    }

    //maybe not good design? Should we at least return copy?
    public WhiteCard getChosenCard(){
        return chosenCard;
    }


    public WhiteCard getWhiteCardById(long id) throws Exception{
        for(WhiteCard whiteCard:hand) {
            if (whiteCard.getId() == id) {
                return whiteCard;
            }
        }
        //specify better exception later
        throw new Exception();
    }
}
