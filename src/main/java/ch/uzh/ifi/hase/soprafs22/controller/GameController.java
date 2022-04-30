package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.MatchStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.*;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
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
import java.util.HashMap;
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

    //NOW IN USER CONTROLER
/*    //Adds a user to the list of all current players in the lobby
    @PostMapping("/lobbies/{lobbyId}/lists/players/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<User>> addUserToLobby(@PathVariable long lobbyId, @PathVariable long userId){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        String baseErrorMessage2 = "The same user is already existing in the lobby ";
        User user = gameService.findUserById(userId);
        //User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        try {
            gameService.addPlayerToLobby(lobbyId, user);
            ArrayList<User> allUsers = gameManager.getLobby(lobbyId).getCurrentPlayers();
            return ResponseEntity.ok(allUsers);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
        catch (Exception e2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage2);
        }
    }*/
//test
    //Removes a user from the list of all current players in the lobby
    @DeleteMapping("/lobbies/{lobbyId}/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteUserFromLobby(@PathVariable long lobbyId, @RequestBody UserPostDTO userPostDTO){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        String baseErrorMessage2 = "No such user exists in the lobby";
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        try {
            gameService.removePlayerFromLobby(lobbyId, userInput);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
        catch (Exception e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, baseErrorMessage2);
        }
    }

/*
    //Retrieves if the smallest number of players is reached or not
    @GetMapping("/lobbies/{lobbyId}/players/minimum")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Boolean> reachedMinimumNumberOfPlayers(@PathVariable long lobbyId){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        try {
            boolean outcome = gameService.checkIfMinimumNumberOfPlayers(lobbyId);
            return ResponseEntity.ok(outcome);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }
*/


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

    //Creates a lobby and returns the id of the newly created lobby //
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
    @PostMapping("/matches/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Long> startingMatch(@PathVariable long lobbyId){
        String baseErrorMessage1 = "Match could not be created";
        try {
            Match newMatch = gameService.startMatch(lobbyId);
            newMatch.setMatchStatus(MatchStatus.MatchOngoing);
            return ResponseEntity.ok(newMatch.getId());
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
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


    //NOW IN USERCONTROLLER
    //ARTIFICIALLY CREATE MATCH -> DELETE LATER
   /* @PostMapping("matches/{matchId}/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void TEST_createMatch(@PathVariable long matchId) throws Exception{
        ArrayList<User> testAllUsers = (ArrayList<User>) gameService.test_getUsers();
        gameManager.createMatch(testAllUsers, matchId);
        BlackCard black = new BlackCard();
        black.createCard();

        Match currentMatch = gameManager.getMatch(matchId);
        currentMatch.getRound().setBlackCard(black);
    }*/

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
            gameService.incrementCardScore(matchId, cardOwnerId);
        }
        catch (IncorrectIdException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }

    //gets current state of countdown. Is used in frontend to know when to get redirected
    @GetMapping("/matches/{matchId}/countdown")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Integer> getCountdown(@PathVariable long matchId) throws Exception {
        Match currentMatch  = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        int currentTime = currentRound.getCountdown().getTime(); //gets remaining time from round countdown
        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(currentTime);
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
    //retrieves the ranking of the players
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
/* 7464
    //updates the round such that next round can be played
    @PutMapping("matches/{matchId}/rounds")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateRound(@PathVariable long matchId) throws Exception{
        Match currentMatch = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        currentRound.startNewRound();

    }

 */



}


