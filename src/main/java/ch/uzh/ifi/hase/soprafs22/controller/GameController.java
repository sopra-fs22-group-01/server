package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
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
    public void deleteUserFromLobby(@PathVariable long lobbyId){
        //gameService
    }

    //Retrieves if the smallest number of players is reached or not
    @GetMapping("/lobbies/{lobbyId}/players/minimum")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void reachedMinimumNumberOfPlayers(@PathVariable long lobbyId){

    }

    //Updates the ready status of a certain user
    @PutMapping("/lobbies/{lobbyId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateReadyStatus(@PathVariable long lobbyId, @PathVariable long userId){

    }

    //Retrieves if all current players in the lobby are ready or not
    @GetMapping("/lobbies/{lobbyId}/status")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void checkIfMatchCanGetStarted(@PathVariable long lobbyId){

    }

    //Puts all players from the lobby to the game (by emptying the lobby list, setting the game player list and creating the players hand)
    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startingMatch(@PathVariable long lobbyId){

    }

}



