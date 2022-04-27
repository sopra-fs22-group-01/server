package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class creates a round for players in the lobby
 * Responsibilities: Round Logic
 * - creates a new black card for new Round
 * - updates players' hands
 * - determines the winner
 * To Do:
 *      creating the hands of the round
 * */

public class Round {
    //Round always displays a unique blackCard at the beginning of the round
    private BlackCard blackCard;
    //Chosen cards are the cards that are played by the players and that will get displayed to vote
    private ArrayList<WhiteCard> chosenCards = new ArrayList<>();
    //Saves the hand of each player. Each player is the owner of a hand.
    private ArrayList<Hand> hands;


    public Round(ArrayList<User> players) {
        this.blackCard = new BlackCard();
        createHands(players);
    }

    public void startNewRound(){
        // setting the new black card of the round
        this.blackCard.createCard();

        // Updating the hand of each player by handing out one card
        updateHands();

        // deleting chosenCards by clearing the Array chosenCards and setting the chosenCards of each Hands to null
        chosenCards.clear();
        for (Hand hand : hands){
            hand.setChosenCard(null);
        }
    }

    //can get deleted when everything implemented?
    public void setBlackCard(BlackCard blackCard){
        this.blackCard = blackCard;
    }

    public BlackCard getBlackCard(){return this.blackCard;}

    public Hand getHandByUserId(Long userId){
        String errorMsg = "Hand not found";

        for (Hand hand: hands){
            if (Objects.equals(hand.getOwner().getId(), userId)){
                return hand;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
    }


    // Player wants to play with a card
    public void setChosenCard(WhiteCard whiteCard){
        // setting the chosenCard of the hand
        for (Hand hand : hands){
            if (hand.getOwner().equals(whiteCard.getOwner())){
                hand.setChosenCard(whiteCard);
            }
        }
        //adding the chosen card to the array of all chosen cards
        chosenCards.add(whiteCard);
    }

    public ArrayList<WhiteCard> getAllChosenCards(){ return chosenCards;}

    private void createHands(ArrayList<User> players){
        for (User player: players) {
            Hand hand = new Hand(player);
            hand.createHand();
            hands.add(hand);
        }
    }

    // adds a new card to the players' hand
    public void updateHands(){
        // for each hand, add a new card at the start of a round since each player lost one card in the last round
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
