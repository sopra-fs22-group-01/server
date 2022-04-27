package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.Hand;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;

import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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

    //Adds a user to the list of all current players in the lobby
    @PostMapping("/lobbies/{lobbyId}/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void addUserToLobby(@PathVariable long lobbyId, @RequestBody UserPostDTO userPostDTO){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        String baseErrorMessage2 = "The same user is already existing in the lobby ";
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        try {
            gameService.addPlayerToLobby(lobbyId, userInput);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
        catch (Exception e2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage2);
        }
    }

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

    //Updates the ready status of a certain user
    @PutMapping("/lobbies/{lobbyId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateReadyStatus(@PathVariable long lobbyId, @PathVariable long userId, @RequestBody ReadyStatus readyStatus){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        try {
            gameService.updateUserReadyStatus(lobbyId, userId, readyStatus);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
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

    //Puts all players from the lobby to the game (by setting the game player list and creating the players hand)
    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startingMatch(@PathVariable long lobbyId){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        try {
            gameService.startMatch(lobbyId);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }

    //Creates a lobby and returns the id of the newly created lobby
    //HttpStatus should be .Created but ResponseEntity.created doesn't have a body,
    //therefore it wouldn't be possible to return the id
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Lobby> createNewLobby(){
        //creates a lobby
        Lobby newLobby = gameService.createNewLobby();
        return ResponseEntity.ok(newLobby);
    }

    //Temporary endpoint
    //Retrieves all ids from the existing lobbies
    //Returns an Array with these ids
    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<Lobby>> getAllLobbiesId(){
        ArrayList<Lobby> allLobbies=gameManager.getAllLobby();
        return ResponseEntity.ok(allLobbies);
    }

    /*
    // check if all users are Ready
    @GetMapping("/lobby/status")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<LobbyStatus> getLobbyStatus() throws Exception {

        LobbyStatus lobbyStatus = gameService.getLobbyStatus();

        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(lobbyStatus);
    }
     */

    //ARTIFICIALLY CREATE MATCH -> DELETE LATER
    @PostMapping("matches/{matchId}/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void TEST_createMatch(@PathVariable long matchId) throws Exception{
        ArrayList<User> testAllUsers = (ArrayList<User>) gameService.test_getUsers();
        gameManager.createMatch(testAllUsers, matchId);
        BlackCard black = new BlackCard();
        black.createCard();

        Match currentMatch = gameManager.getMatch(matchId);
        currentMatch.getRound().setBlackCard(black);
    }

    // retrieve text for black Card by matchId
    @GetMapping("/matches/{matchId}/blackCard")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> getBlackCard(@PathVariable long matchId) throws Exception {
        BlackCard b = gameService.getBlackCard(matchId);
        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(b.getText());
    }

    //increments the score of a white card by one
    @PutMapping("matches/{matchId}/white-cards/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void incrementWhiteCard(@PathVariable long matchId, @PathVariable long cardId) throws Exception{
        gameService.incrementCardScore(matchId,cardId);
    }

    // get hand by userid
    @GetMapping("/matches/{matchId}/hands/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Hand> getHand(@PathVariable long matchId, @PathVariable long userId) throws Exception {
        ArrayList<User> allUsers = (ArrayList<User>) gameService.test_getUsers();

        gameManager.createMatch(allUsers,matchId);
        Match test_match = gameManager.getMatch(matchId);
        Hand test_hand = test_match.getHandByUserId(userId);

        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(test_hand);
    }

}


