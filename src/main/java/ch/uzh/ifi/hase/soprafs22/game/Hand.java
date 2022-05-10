package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.Card;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Objects;

//Not yet sure if Hand should be in game//TEST a
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
    public void setChosenCard(WhiteCard card){
        System.out.println("This is the text of the chosen card: " + card.getText());
        this.chosenCard = card;}

    public User getOwner(){return this.owner;}
    public void setOwner(User user){this.owner = user;} // test}

    public Hand(User owner) {
        this.chosenCard = new WhiteCard(owner);
        this.owner = owner;
    }

    //should draw ten white cards from the deck and save it in the hand
    public void createHand(){
        int numberOfCards = 0;
        if (this.owner.getCustomWhiteText() != null){
            WhiteCard customCard = new WhiteCard(this.owner);
            customCard.createCard();
            customCard.setText(this.owner.getCustomWhiteText());
            this.userHand.add(customCard);
            numberOfCards = 1;
        }
        while (numberOfCards < 10){
            WhiteCard newCard = new WhiteCard(this.owner);
            newCard.createCard(); //setting owner and text for the card
            boolean cardWithSameText = false;
            for (WhiteCard card: userHand){
                if(Objects.equals(newCard.getText(), card.getText())){
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

    //adds new card to hand and removes last played card
    public void updateHand(){
        //removes chosenCard (card which got played in last round) form hand

        for(WhiteCard whiteCard : this.userHand){
            if(this.chosenCard != null && whiteCard.getText().equals(this.chosenCard.getText())){
                this.userHand.remove(whiteCard);
                System.out.println("card got removed from user hand");
                break;
            }
        }
        System.out.println("after removal should have happened-----------------");
        WhiteCard card = new WhiteCard(owner);
        card.createCard();
        this.userHand.add(card);
    }

    //sets chosenCard to null
    public void resetChosenCard(){
        this.chosenCard = null;
    }

    public ArrayList<WhiteCard> getCardsFromHand (){
        ArrayList<Card> cards;
        return this.userHand;
    }

}
