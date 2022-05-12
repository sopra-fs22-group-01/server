
package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.MatchStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ApiRequestStatus;

import java.util.ArrayList;


public class Match {
    private Long id;
    private ArrayList<User> matchPlayers = new ArrayList<>();

    private ScoreBoard scoreBoard;
    private Round round;
    private MatchStatus matchStatus;

    private ApiRequestStatus apiRequestStatus;
    private int requestCount;


    private boolean scoresAlreadyUpdated;

    public Match(Long id) {
        this.id = id;
        this.matchStatus = MatchStatus.MatchOngoing;
        this.apiRequestStatus = ApiRequestStatus.INCOMPLETE;
        this.requestCount = 0;
        this.scoresAlreadyUpdated = false;
    }

    public void createRound(){
        //initializes the round with the players of the match, creates automatically the  hands
        round = new Round(matchPlayers);
    }

    public void setMatchStatus(MatchStatus matchStatus){this.matchStatus = matchStatus;}
    public MatchStatus getMatchStatus(){return this.matchStatus;}

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public boolean isScoresAlreadyUpdated() {
        return scoresAlreadyUpdated;
    }

    public void setScoresAlreadyUpdatedTrue() {
        this.scoresAlreadyUpdated = true;
    }


    public void setScoresAlreadyUpdatedFalse() {
        this.scoresAlreadyUpdated = false;
    }

    public Round getRound(){return round;}

    public ArrayList<User> getMatchPlayers(){
        return matchPlayers;
    }
    public void setMatchPlayers(ArrayList<User> users){
        this.matchPlayers = users;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){this.id = id;}

    private void setApiRequestStatus(ApiRequestStatus apiRequestStatus) {
        this.apiRequestStatus = apiRequestStatus;
    }

    public ApiRequestStatus getApiRequestStatus() {
        return this.apiRequestStatus;
    }

    public void resetApiRequestStatus(){
        this.apiRequestStatus = ApiRequestStatus.INCOMPLETE;
    }


    public void createScoreBoard() {
        this.scoreBoard = new ScoreBoard();
    }

    public int getPlayerCount(){
        return this.matchPlayers.size();
    }

    // gets winnerCards from last rounds to update all scores of players, but not in Database
    public void updatePlayerScores(){
        ArrayList<WhiteCard> winnerCards = this.round.getRoundWinnerCards();
        for(WhiteCard whiteCard : winnerCards){
           for(User user : this.matchPlayers){
               if (whiteCard.getOwner().getId() == user.getId()){
                   int oldScore = user.getScore();
                   user.setScore(oldScore+1);
               }
           }
        }
        this.setScoresAlreadyUpdatedTrue();
    }

    public void incrementRequestCountAndCheckStatus(){
        this.requestCount++;
        int numberOfPlayers = this.getPlayerCount();
        if(requestCount == numberOfPlayers){
            setApiRequestStatus(ApiRequestStatus.COMPLETE);
        }

    }

}