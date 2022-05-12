package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.*;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ApiRequestStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Ranking;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;

import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;
    GameManager gameManager = GameManager.getInstance();


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/rules")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<String>> getRules() throws Exception {

        ArrayList<String> rules = gameService.getRulesFromText();

        //.ok sets the HTTP status to OK (200)/
        return ResponseEntity.ok(rules);
    }

    //Removes a user from the list of all current players in the lobby
    @DeleteMapping("/lobbies/{lobbyId}/players/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteUserFromLobby(@PathVariable long lobbyId, @PathVariable long userId){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        String baseErrorMessage2 = "No such user exists in the lobby";
        //User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        try {
            gameService.removePlayerFromLobby(lobbyId, userId);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
        catch (Exception e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, baseErrorMessage2);
        }
    }

    //Retrieves if all current players in the lobby are ready or not and if the minimum number of player was reached
    @GetMapping("/lobbies/{lobbyId}/status")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<LobbyStatus> checkIfMatchCanGetStarted(@PathVariable long lobbyId){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        try {
            gameService.checkIfLobbyStatusChanged(lobbyId);
            LobbyStatus lobbyStatus = gameService.getLobbyStatus(lobbyId);
            return ResponseEntity.ok(lobbyStatus);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }

    //Creates a lobby and returns newly created lobby //
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody//
    public ResponseEntity<Lobby> createNewLobby(){
        //creates a lobby
        Lobby newLobby = gameService.createNewLobby();
        return ResponseEntity.ok(newLobby);
    }


    //Retrieves all existing lobbies
    //Returns an Array with these lobbies
    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<Lobby>> getAllLobbies(){
        ArrayList<Lobby> allLobbies=gameManager.getAllLobby();
        return ResponseEntity.ok(allLobbies);
    }

    //Retrieves all users from a specific lobby
    //Returns an Array of users
    @GetMapping("/lobbies/{lobbyId}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<User>> getAllUsersByLobbyId(@PathVariable long lobbyId) {
        String baseErrorMessage1 = "Couldn't retrieve lobby users";
        try {
            ArrayList<User> allLobbyUsers = gameManager.getLobby(lobbyId).getCurrentPlayers();
            return ResponseEntity.ok(allLobbyUsers);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }

    //Creates a new match and puts all players from the lobby into it
    //Should delete the lobby
    @PostMapping("/matches/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatchGetDTO startingMatch(@PathVariable long lobbyId){
        String baseErrorMessage1 = "Match could not be created";
        try {
            Match newMatch = gameService.startMatch(lobbyId);
            return DTOMapper.INSTANCE.convertEntityToMatchGetDTO(newMatch);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }

    //retrieves the round number of the match and return it
    @GetMapping("/matches/{matchId}/roundnumbers")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Integer> getRoundNumberSpecificMatch(@PathVariable long matchId) throws IncorrectIdException {
        //fetch the specific round number
        Match currentMatch = gameManager.getMatch(matchId);
        int roundNumber=currentMatch.getRound().getRoundNumber();
        return ResponseEntity.ok(roundNumber);
    }

    //retrieves all users from a specific match and returns array of userGetDTO
    @GetMapping("/matches/{matchId}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsersFromSpecificMatch(@PathVariable long matchId) throws IncorrectIdException {
        // fetch all users in the internal representation
        Match currentMatch = gameManager.getMatch(matchId);
        List<User> matchUsers = currentMatch.getMatchPlayers();

        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : matchUsers) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs; // returns array UserGetDTO with all users init
    }

    // retrieve text for black Card by matchId
    @GetMapping("/matches/{matchId}/blackCard")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> getBlackCard(@PathVariable long matchId) throws Exception {
        BlackCard blackCard = gameService.getBlackCard(matchId);
        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(blackCard.getText());
    }

    //increments the score of a white card by one
    @PutMapping("matches/{matchId}/white-cards/{cardOwnerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void incrementWhiteCard(@PathVariable long matchId, @PathVariable long cardOwnerId){
        String baseErrorMessage1 = "Wrong ID, Couldn't retrieve the match";
        try {
            if (cardOwnerId >= 0){
                gameService.incrementCardScore(matchId, cardOwnerId);
            }

        }
        catch (IncorrectIdException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }



    //gets current state of countdown. Is used in frontend to know when to get redirected
    @GetMapping("/matches/{matchId}/countdown/selection")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Integer> getSelectionCountdown(@PathVariable long matchId) throws Exception {
        Match currentMatch  = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        int currentTime = currentRound.getSelectionTime(); //gets remaining time from round countdown
        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(currentTime);
    }


    //gets current state of countdown. Is used in frontend to know when to get redirected
    @GetMapping("/matches/{matchId}/countdown/voting")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Integer> getVotingCountdown(@PathVariable long matchId) throws Exception {
        Match currentMatch  = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        int currentTime = currentRound.getVotingTime(); //gets remaining time from round countdown
        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(currentTime);
    }

    //gets current state of countdown. Is used in frontend to know when to get redirected
    @GetMapping("/matches/{matchId}/countdown/roundwinners")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Integer> getRoundWinnersCountdown(@PathVariable long matchId) throws Exception {
        Match currentMatch  = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        int currentTime = currentRound.getRankingTime(); //gets remaining time from round countdown
        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(currentTime);
    }

    //starts selectionCountdown in round (will only be started once until timer is 0 again)
    @PutMapping("/matches/{matchId}/countdown/selection")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startSelectionCountdown(@PathVariable long matchId) throws Exception {
        Match currentMatch  = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        currentRound.startSelectionCountdown(); //starts selection countdown
    }

    //starts voting Countdown in round (will only be started once until timer is 0 again)
    @PutMapping("/matches/{matchId}/countdown/voting")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startVotingCountdown(@PathVariable long matchId) throws Exception {
        Match currentMatch  = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        currentRound.startVotingCountdown(); //starts voting countdown
    }

    //starts ranking Countdown in round (will only be started once until timer is 0 again)
    @PutMapping("/matches/{matchId}/countdown/roundwinners")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startRoundWinnersCountdown(@PathVariable long matchId) throws Exception {
        Match currentMatch  = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        currentRound.startRankingCountdown(); //starts ranking countdown
    }


    // put selected white card from hand into array of allChosenCards with matchId
    @PutMapping("/matches/{matchId}/white-card/selection")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public ResponseEntity<ArrayList<WhiteCard>> addChosenCard(@PathVariable long matchId, @RequestBody WhiteCardPutDTO whiteCardPutDTO) throws Exception{
        Match currentMatch = gameManager.getMatch(matchId);
        currentMatch.getRound().setChosenCard(DTOMapper.INSTANCE.convertWhiteCardPutDTOToEntity(whiteCardPutDTO));

        // return array for debugging reasons -> delete later
        return ResponseEntity.ok(currentMatch.getRound().getAllChosenCards());
    }

    // get all chosen card from matchId
    @GetMapping("/matches/{matchId}/election/white-cards")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<WhiteCard>> getChosenCards(@PathVariable long matchId) throws Exception{
        Match currentMatch = gameManager.getMatch(matchId);
        // return array for debugging reasons -> delete later
        return ResponseEntity.ok(currentMatch.getRound().getAllChosenCards());
    }



    //retrieves the ranking of the players
    @GetMapping("/matches/{matchId}/scores")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<Ranking>> GetRankingOfAllPlayer(@PathVariable long matchId){
        String baseErrorMessage1 = "Wrong ID, Couldn't retrieve the match";
        try {
            ArrayList<Ranking> ranking = gameService.getRanking(matchId);
            return ResponseEntity.ok(ranking);
        }
        catch (IncorrectIdException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }//

    // retrieve currentRound winnerCards
    @GetMapping("/matches/{matchId}/winner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<WhiteCard>> getWinner(@PathVariable long matchId){
        String baseErrorMessage1 = "Wrong ID, Couldn't retrieve the winner";
        try {

            ArrayList<WhiteCard> winnerCards = gameManager.getMatch(matchId).getRound().getRoundWinnerCards();
            return ResponseEntity.ok(winnerCards);
        }
        catch (IncorrectIdException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }

    //incrementsApiRequestCount and if all put request arrived, sets ApiRequestStatus to COMPLETE
    @PutMapping("/matches/{matchId}/synchronization")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void incrementApiRequestCount(@PathVariable long matchId) throws IncorrectIdException {
        Match currentMatch = gameManager.getMatch(matchId);
        currentMatch.incrementRequestCountAndCheckStatus();
    }

    @GetMapping("/matches/{matchId}/synchronization")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ApiRequestStatus> getApiRequestStatus(@PathVariable long matchId) throws IncorrectIdException {
        Match currentMatch = gameManager.getMatch(matchId);
        ApiRequestStatus currentApiRequestStatus = currentMatch.getApiRequestStatus();

        return ResponseEntity.ok(currentApiRequestStatus);
    }

}


