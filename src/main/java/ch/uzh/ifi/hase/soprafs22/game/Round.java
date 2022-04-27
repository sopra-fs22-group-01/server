package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;

import java.util.ArrayList;

/**
 * This class creates a round for players in the lobby
 * Responsibilities: Round Logic
 * - creates a new black card for new Round
 * - updates players' hands
 * - determines the winner
 *
 * */

public class Round {

    private BlackCard blackCard;

    // cards that are played by the players (cards in the middle)
    private ArrayList<WhiteCard> chosenCards = new ArrayList<>();
    private ArrayList<Hand> hands;


    public Round(ArrayList<Hand> hands) {
        this.blackCard = new BlackCard();
        this.hands = hands;
    }

    public void startNewRound(){
        //setting new black card
        this.blackCard.createCard();
        // Updating the hand of each player by handing out one card
        updateHands();
        //deleting chosenCards: clear Array chosenCards, chosenCards of each Hands setting null
        chosenCards.clear();
        for (Hand hand : hands){
            hand.setChosenCard(null);
        }
    }

    public void setBlackCard(BlackCard blackCard){
        this.blackCard = blackCard;
    }

    public BlackCard getBlackCard(){return this.blackCard;}


    // Player wants to play with a card
    public void setChosenCard(WhiteCard whiteCard){
        //updating the chosenCard of the hand
        for (Hand hand : hands){
            if (hand.getOwner().equals(whiteCard.getOwner())){
                hand.setChosenCard(whiteCard);
            }
        }
        //adding the chosen card to the array of all chosen cards
        chosenCards.add(whiteCard);
    }

    public ArrayList<WhiteCard> getAllChosenCards(){ return chosenCards;}

    // add a new card to the players' hand
    public void updateHands(){
        // for each hand, add a new card after each round since players already played with it
        for (Hand hand: hands){
            hand.updateHand();
        }
    }

    // Determines the winner of the round
    // Returns an ArrayList with winner cards
    public ArrayList<WhiteCard> getRoundWinner(){
        ArrayList<WhiteCard> highestScoreCards = new ArrayList<>();
        int currentHighScore = 0;

        for (WhiteCard chosenCard : chosenCards) {
            if (chosenCard.getScore() == currentHighScore) {
                highestScoreCards.add(chosenCard);
            } else if (chosenCard.getScore() > currentHighScore){
                currentHighScore = chosenCard.getScore();
                highestScoreCards.clear();
                highestScoreCards.add(chosenCard);
            }
        }
        return highestScoreCards;
    }
}
