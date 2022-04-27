package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;

import java.util.ArrayList;

/**
 * This class creates a round for players in the lobby
 * Responsibilities:
 *
 * */

public class Round {

    private BlackCard blackCard;
    //private ArrayList<User> roundPlayers = new ArrayList<>();

    // cards that are played by the players (cards in the middle)
    private ArrayList<WhiteCard> chosenCards = new ArrayList<>();
    private ArrayList<Hand> hands;

    protected boolean isRoundFinished = false; //Enum? Needed?

    public Round(ArrayList<Hand> hands) {
        this.blackCard = new BlackCard();
        this.hands = hands;
    }

    // add a new card to the players' hand
    public void updateHands(){
        // for each hand, add a new card after each round since players already played with it
        for (Hand hand: hands){
            hand.updateHand();
        }
    }

    public void startNewRound(){
        //setting new black card
        this.blackCard.createCard();
        // Updating the hand of each player by handing out one card
        // updateHand();
        //deleting chosenCards: clear Array chosenCards, chosenCards of each Hands setting null
    }

    /*
    public ArrayList<User> getRoundPlayers(){
        return roundPlayers;
    }

    public void setRoundPlayers(ArrayList<User> givenPlayers){
        this.roundPlayers = givenPlayers;
    }
     */

    public void setBlackCard(BlackCard blackCard){
        this.blackCard = blackCard;
    }

    public BlackCard getBlackCard(){return this.blackCard;}


    // Player wants to play with a card
    public void setChosenCard(WhiteCard whiteCard){
        chosenCards.add(whiteCard);
    }

    public ArrayList<WhiteCard> getAllChosenCards(){
        //iterate over each hand and get the chosen cards and append them to the chosenCards list.
        for(Hand hand: hands){
            chosenCards.add(hand.getChosenCard());
        }
        return chosenCards;
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
        isRoundFinished = true;
        return winnerCard.getOwner();
    }
}
