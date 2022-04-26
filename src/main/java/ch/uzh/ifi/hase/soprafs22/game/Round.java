package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.Card;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
//
import java.util.ArrayList;

public class Round {

    private BlackCard blackCard;
    //private ArrayList<User> players;

    // cards that are played by the players (cards in the middle)
    private ArrayList<WhiteCard> chosenCards;
    private ArrayList<Hand> hands;

    public Round(ArrayList<Hand> hands) {
        this.hands = hands;
    }

    public void startNewRound(){}

    public void setRoundPlayers(){}

    public void setBlackCard(){}

    public void getBlackCard(){}

    public void updateHands(){} //Hand.addingCard();

    public void setChosenCard(long cardId, User owner){}

    public ArrayList<WhiteCard> getAllChosenCards(){
        //just example, so something gets returned
        return new ArrayList<WhiteCard>();
    }

    // return the owner of the white card with the highest score
    public User getRoundWinner(){
        WhiteCard winnerCard = new WhiteCard();
        winnerCard.setScore(0);
        for (WhiteCard chosenCard : chosenCards) {
            if (chosenCard.getScore() > winnerCard.getScore()) {
                winnerCard = chosenCard;
            }
        }
        return winnerCard.getOwner();
    }



}
