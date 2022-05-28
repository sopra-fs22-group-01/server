package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Countdown;
import ch.uzh.ifi.hase.soprafs22.game.helpers.StartRoundStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Objects;

/**
 *  This class creates a round for players in the lobby
 * Responsibilities: Round Logic
 * - creates a new black card for new Round
 * - updates players' hands
 * -  determin es the winner
 * To Do:
 *      creating the hands of the  round
 * */

public class Round {
    //Round always displays a unique blackCard at the beginning of the round
    private BlackCard blackCard;
    //Chosen cards are the cards that are played by the players and that will get displayed to vote
    private ArrayList<WhiteCard> chosenCards = new ArrayList<>();
    //Saves the hand of each player. Each player is the owner of a hand.
    private ArrayList<Hand> hands = new ArrayList<>();
    private int roundNumber;



    private int maxScore = 5;

    //countdown of a specific round
    private Countdown selectionCountdown ;
    private Countdown votingCountdown;
    private Countdown rankingCountdown;

    public int getRoundNumber() {
        return this.roundNumber;
    }


    StartRoundStatus startRoundStatus;

    // Constructor
    public Round(ArrayList<User> players) {
        this.blackCard = new BlackCard();
        this.blackCard.createCard();
        createHands(players);
        this.roundNumber = 1;
        //this.roundCountdown = new Countdown(); // singleton, otherwise a countdown for each player would be created

        selectionCountdown = new Countdown(45);
        // time for TTS, 5 seconds for each white card and 10 seconds for the black card
        votingCountdown = new Countdown(hands.size() * 5 + 10);
        rankingCountdown = new Countdown(15);

        startRoundStatus = StartRoundStatus.PENDING;
        //starts first selection countdown
        //remove after and start it with endpoint
    }


    public int getMaxScore() {
        return maxScore;
    }

    public int getSelectionTime(){
        return this.selectionCountdown.getCurrentTime();
    }
    public int getRankingTime(){
        return this.rankingCountdown.getCurrentTime();
    }
    public int getVotingTime(){
        return this.votingCountdown.getCurrentTime();
    }

    public void startSelectionCountdown(){
        this.selectionCountdown.startCountdown();
    }
    public void startRankingCountdown(){
        ArrayList<WhiteCard> roundWinners = getRoundWinnerCards();
        int numberOfWinners = roundWinners.size();

        //checking if only one or zero winner
        if(numberOfWinners <= 1){
            //setTimer to 15 and startCountdown
            this.rankingCountdown.setTime(10);
            this.rankingCountdown.startCountdown();
        }
        else {
            //setTimer depending on how many winner exist and start countdown
            int adjustedTime = numberOfWinners * 10;
            this.rankingCountdown.setTime(adjustedTime);
            this.rankingCountdown.startCountdown();
        }
    }
    public void startVotingCountdown(){
        this.votingCountdown.startCountdown();
    }



    //startNewRound gets called from gameManager after it checked if the game is over or not yet
    public void startNewRound(){
            if(startRoundStatus == StartRoundStatus.PENDING){

                startRoundStatus = StartRoundStatus.STARTED;
                // returns true if keep playing
                this.roundNumber++;

                // setting the new black card of the round
                this.blackCard.createCard();

                // Updating the hand of each player by handing out one card
                updateHands();

                // deleting chosenCards by clearing the Array chosenCards and setting the chosenCards of each Hands to null
                chosenCards.clear();
                //and setting the chosenCards of each Hands to null by calling the resetChosenCard function
                for (Hand hand : hands){
                    hand.resetChosenCard();
                }
            }
    }

    public void resetStartRoundStatus(){
        this.startRoundStatus = StartRoundStatus.PENDING;
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
    public ArrayList<Hand> getHands(){
        return this.hands;

    }

    // Player wants to play with a card
    public void setChosenCard(WhiteCard whiteCard){
        System.out.println("Card in Round to be set as chosenCard contains the text " + whiteCard.getText());
        // setting the chosenCard of the hand
        for (Hand hand : hands){
            if (hand.getOwner().getId().equals(whiteCard.getOwner().getId())){ //checks if owner of whiteCard equals owner of hand
                System.out.println("Owner of the chosen card = " + whiteCard.getOwner() + " and text is " + whiteCard.getText());
                hand.setChosenCard(whiteCard);
            }
        }

        //adding the chosen card to the array of all chosen cards
        //if chosenCard.owner in chosenCards -- do nothing
        if(!chosenCards.isEmpty()){
            for(WhiteCard card :chosenCards){
                if(card.getOwner().getId().equals(whiteCard.getOwner().getId())){
                    return;
                }
            }
        }
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
    public ArrayList<WhiteCard> getRoundWinnerCards(){
        ArrayList<WhiteCard> highestScoreCards = new ArrayList<>();
        int currentHighScore = 0;

        for (WhiteCard chosenCard : this.chosenCards) {
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

    public void incrementCardScores(long searchedCardOwnerId){ //
        //iterates through all chosen cards and increments the wanted card by 1
        for(WhiteCard whiteCard : this.chosenCards){
            // in chosenCard is max. one card per player as each player can only choose one card
            // therefore it's possible to identify the card with the owner's ID
            long cardOwnerId = whiteCard.getOwner().getId();
            if(cardOwnerId == searchedCardOwnerId){
                whiteCard.incrementCard(); //increments the card score by 1
            }
        }
    }
}
