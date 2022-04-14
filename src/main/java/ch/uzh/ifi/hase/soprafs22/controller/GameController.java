package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/rules")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<String>> getRules() throws Exception {

        ArrayList<String> rules = gameService.getRulesFromText();

        //.ok sets the HTTP status to OK (200)
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

    //Retrieves if all current players in the lobby are ready or not
    @GetMapping("/lobbies/{lobbyId}/status")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean checkIfMatchCanGetStarted(@PathVariable long lobbyId){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        try {
            boolean outcome = gameService.checkIfAllPlayersReady(lobbyId);
            return outcome;
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

}



