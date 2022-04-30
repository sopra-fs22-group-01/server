
package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.GameStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Countdown;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;

/**
 * 
 * */

public class Match {
    private Long id;
    private ArrayList<User> players = new ArrayList<>();
    //private ArrayList<Hand> allPlayersHands = new ArrayList<>();

    private ScoreBoard scoreBoard;
    private Timer timer;
    private Round round;

    public Match(Long id) {
        this.id = id;
    }

    public void createRound(){
        //initializes the round with the players of the match, creates automatically the hands
        round = new Round(players);
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }


    public Timer getTimer() {
        return timer;
    }

    public Round getRound(){
        return round;
    }

    public ArrayList<User> getMatchPlayers(){
        return players;
    }

    public void setMatchPlayers(ArrayList<User> users){
        this.players = users;
    }

    public Long getId() {
        return id;
    }


    public void createScoreBoard() {
        this.scoreBoard = new ScoreBoard();
    }


    // gets winnerCards from last rounds to update all scores of players, but not in Database
    public void updatePlayerScores(){
        ArrayList<WhiteCard> winnerCards = this.round.getRoundWinnerCards();
        for(WhiteCard whiteCard : winnerCards){
           for(User user : this.players){
               if (whiteCard.getOwner().getId() == user.getId()){
                   int oldScore = user.getScore();
                   user.setScore(oldScore+1);
               }//
           }
        }
    }

    /*
    // creating a hand with 10 cards
    public void createHands(){
        for (User player: players) {
            Hand hand = new Hand(player);
            hand.createHand();
            allPlayersHands.add(hand);
        }
    }

    public Hand getHandByUserId(Long userId){
        String errorMsg = "Hand not found";

        for (Hand hand: allPlayersHands){
            if (Objects.equals(hand.getOwner().getId(), userId)){
                return hand;
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);

    }
    */

    /*
    // The method increases the winners' score
    // It also tackles the edge case where there are multiple winners, their score are increased together
    public void updateScoreBoard(){
        ArrayList<WhiteCard> roundWinners = round.getRoundWinnerCards();
        for(WhiteCard whiteCard: roundWinners){
            User winner = whiteCard.getOwner();
            int scoreCard = whiteCard.getScore();
            scoreBoard.updateScore(winner, scoreCard);
        }
    }
     */

}