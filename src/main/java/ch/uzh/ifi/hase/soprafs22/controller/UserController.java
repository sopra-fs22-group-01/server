package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Hand;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.GameStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;
    private GameService gameService = new GameService();
    private GameManager gameManager = GameManager.getInstance();

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs; // returns array UserGetDTO with all users init
    }


    //gets a specific user from database through the user service and returns it as userGetDTO
    @GetMapping("/users/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUsername(@RequestParam long id) {
        User requestedUser = userService.findUserData(id);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(requestedUser);
    }


    @PostMapping("/users") //creates a User object
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user and logs in (does it make sense to log in here already?)
        User createdUser = userService.createUser(userInput);
        userService.logInUser(createdUser);


        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }


    @PostMapping("/users/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loggIn(@RequestBody UserPostDTO userPostDTO) {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        String baseErrorMessage = "The Password or Username provided is wrong! ";
        // convert each user to the API representation
        for (User user : users) {
            //check if user already exists
            if(userPostDTO.getUsername().equals(user.getUsername())){ //checks if user provide matches a user in DB
                if(!user.getPassword().equals(userPostDTO.getPassword())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, baseErrorMessage);
                }
                userService.logInUser(user);
                //userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
                return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, baseErrorMessage);
    }

    //Maps data from profile changes
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void saveChanges(@RequestBody UserPutDTO userPutDTO, @PathVariable long id) {
        userService.findUserById(id); //throws exception if userid doesnt exist
        userService.updateUser(userPutDTO);
    }


    //mapper for the logout with token
    @PutMapping("/logout/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loggOut(@RequestParam String token) {
        User requestedUser = userService.findUserByToken(token);
        userService.logOutUser(requestedUser);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(requestedUser);
    }

//----------------------------MOVED FROM GAMECONTROLLER TO USERCONTROLLER-------------------------------------

    //Adds a user to the list of all current players in the lobby
    @PostMapping("/lobbies/{lobbyId}/lists/players/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<User>> addUserToLobby(@PathVariable long lobbyId, @PathVariable long userId){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        String baseErrorMessage2 = "The same user is already existing in the lobby ";
        User user = userService.findUserById(userId); // gets user from correct user repository
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
    }

    //ARTIFICIALLY CREATE MATCH -> DELETE LATER
    @PostMapping("matches/{matchId}/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void TEST_createMatch(@PathVariable long matchId) throws Exception{
        ArrayList<User> testAllUsers = (ArrayList<User>) userService.test_getUsers();
        gameManager.createMatch(testAllUsers, matchId);
        BlackCard black = new BlackCard();
        black.createCard();

        Match currentMatch = gameManager.getMatch(matchId);
        currentMatch.getRound().setBlackCard(black);
    }

    // get hand by userid
    @GetMapping("/matches/{matchId}/hands/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Hand> getHand(@PathVariable long matchId, @PathVariable long userId) throws Exception {
        ArrayList<User> allUsers = (ArrayList<User>) userService.test_getUsers();

        gameManager.createMatch(allUsers,matchId);
        Match test_match = gameManager.getMatch(matchId);
        Hand test_hand = test_match.getHandByUserId(userId);

        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(test_hand);
    }

/*
    //Maps data from ready-status changes
    @PutMapping("/lobbies/{lobbyId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateReadyStatus(@PathVariable long lobbyId, @PathVariable long userId) {
        User user = userService.findUserById(userId); //throws exception if userid doesnt exist
        userService.updateUserReadyStatus(user);
    }

    // check if all users are Ready
    @GetMapping("/lobby/status")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<LobbyStatus> getLobbyStatus() throws Exception {

        LobbyStatus lobbyStatus = userService.getLobbyStatus();

        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(lobbyStatus);
    }
*/

}   

