package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.LaughStatus;
import ch.uzh.ifi.hase.soprafs22.constant.MatchStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Hand;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.Round;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.rest.dto.MatchGetDTO;
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
    private final GameService gameService;
    private final GameManager gameManager;

    UserController(UserService userService, GameService gameService, GameManager gameManager) {
        this.userService = userService;
        this.gameService = gameService;
        this.gameManager = gameManager;
    }

    @GetMapping("/users") // tested: ok,
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
        return userGetDTOs;
    }


    //gets a specific user from database through the user service and returns it as userGetDTO
    @GetMapping("/users/") // tested: ok and fail
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUsername(@RequestParam long id) {
        User requestedUser = userService.findUserData(id);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(requestedUser);
    }

    //gets a specific user from database through the user service and returns it as userGetDTO
    @GetMapping("/users/{token}") // tested: ok and fail
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUsernameByToken(@PathVariable String token) {
        User requestedUser = userService.findUserByToken(token);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(requestedUser);
    }


    @PostMapping("/users") //creates a User object
    @ResponseStatus(HttpStatus.CREATED) // tested: failed, ok
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


    @PostMapping("/users/") // tested: ok, failed
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
    @PutMapping("/users/{id}") // tested: ok, fail
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void saveChanges(@RequestBody UserPutDTO userPutDTO, @PathVariable long id) {
        // find by ID to get error if this user doesn't exist
        userService.findUserById(id); //throws exception if userid doesnt exist
        userService.updateUser(userPutDTO);
    }


    //mapper for the logout with token
    @PutMapping("/logout/") // tested, ok , fail
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loggOut(@RequestParam String token) {
        User requestedUser = userService.findUserByToken(token);
        userService.logOutUser(requestedUser);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(requestedUser);
    }


    //Adds a user to the list of all current players in the lobby
    @PostMapping("/lobbies/{lobbyId}/lists/players/{userId}") // tested ok, failed
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<User>> addUserToLobby(@PathVariable long lobbyId, @PathVariable long userId){
        String baseErrorMessage1 = "No lobby with this id could be found.";
        String baseErrorMessage2 = "The same user is already existing in the lobby ";
        User user = userService.findUserById(userId); // gets user from correct user repository
        //User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        try {
            gameManager.removePlayerFromAllLobbies(userId); // no error from here
            gameService.addPlayerToLobby(lobbyId, user); // incorrect exception if lobby not found to add users to
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


    // add bzw. create custom white card to user
    @PutMapping("/matches/{lobbyId}/white-cards/{userId}/custom") // tested: ok, failed (no check for CONFLICT.error)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> createCustomCard(@PathVariable long lobbyId, @PathVariable long userId, @RequestBody UserPutDTO userPutDTO) throws Exception {
        // updates in database
        String text = userService.updateCustomWhiteText(userPutDTO); // bad request if user not found

        // updates in the specific lobby -> not good solution
        gameService.updateCustomText(lobbyId, userId, userPutDTO); // CONFLICT error if lobby not found
        return ResponseEntity.ok(text);
    }

    // get hand by userid
    @GetMapping("/matches/{matchId}/hands/{userId}") // tested: ok, fail
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<WhiteCard>> getHand(@PathVariable long matchId, @PathVariable long userId) throws Exception {
        Match currentMatch = gameManager.getMatch(matchId);
        Round currentRound= currentMatch.getRound();
        Hand playerHand = currentRound.getHandByUserId(userId);
        ArrayList<WhiteCard> playerHandCards = playerHand.getCardsFromHand();

        return ResponseEntity.ok(playerHandCards);
    }

    // get all hands by matchId NEED WHERE?
    @GetMapping("/matches/{matchId}/hands") // NOT TESTED
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<Hand>> getAllHands(@PathVariable long matchId) throws Exception {
        Match currentMatch = gameManager.getMatch(matchId);
        Round currentRound= currentMatch.getRound();
        ArrayList<Hand> allHands = currentRound.getHands();
        return ResponseEntity.ok(allHands);
    }

    //Maps data from ready-status changes
    //used to flip the ready-status
    @PutMapping("/lobbies/{lobbyId}/users/{userId}") // tested: ok, fail
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateReadyStatus(@PathVariable long lobbyId, @PathVariable long userId) {
        String baseErrorMessage1 = "No lobby with this id could be found.";
        try {
            User user = userService.findUserById(userId); //throws exception if userid doesnt exist, NOT FOUND
            userService.updateUserReadyStatus(user); //doesn't update the user in the array of the lobby, only the user in the database
            gameService.updateUserReadyStatus(lobbyId, userId); //updates the user in the array of the lobby, not the perfect solution
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/lobbies/{lobbyId}/users/{userId}/status") // tested: ok, fail
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void resetReadyStatus(@PathVariable long lobbyId, @PathVariable long userId) {
        try {
            User user = userService.findUserById(userId); //throws exception if userid doesnt exist
            userService.resetUserReadyStatus(user); //resets the ReadyStatus of a user in the database
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/users/{userId}/scores") // tested: ok, fail
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void resetUserScore(@PathVariable long userId) {
        try {
            User user = userService.findUserById(userId);
            userService.resetUserScore(user); //finds user in db and resets score to 0
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }



    // checks  status of the match, to know if the game is over or there are still rounds to play
    @GetMapping("/matches/{matchId}/rounds") // tested: ok, fail
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<MatchStatus> checkMatchStatus(@PathVariable long matchId) throws Exception{
        try{
            Match currentMatch = gameManager.getMatch(matchId);
            MatchStatus currentMatchStatus = currentMatch.getMatchStatus();
            if(currentMatchStatus == MatchStatus.MatchOngoing){
                gameManager.startNewRound(matchId);
            }
            return ResponseEntity.ok(currentMatchStatus);
            // return true if new round gets started, false if match is over (starts new round)

        }catch (IncorrectIdException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //updates the scores such that next round can be played bzw. end game if it was last round
    @PutMapping("/matches/{matchId}/scores/{userId}") // tested: ok (with MatchOngoing, GameOver), fail
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateScores(@PathVariable long matchId, @PathVariable long userId) throws Exception {
        try{
            Match currentMatch = gameManager.getMatch(matchId); // incorredctIdexception "The match was not found"


            if (!currentMatch.isScoresAlreadyUpdated()) {
                // update player scores in currentMatch, not userService, so we don't receive a COPY of a playersArray
                currentMatch.updatePlayerScores();

                Round currentRound = currentMatch.getRound();

                //makes sure a new round can later be started by one player
                currentRound.resetStartRoundStatus();

                // gets winnerCards from last rounds to update all scores in database
                userService.updateScores(currentRound.getRoundWinnerCards()); // void method

                boolean keepPlaying = gameManager.evaluateNewRoundStart(matchId); // checks if someone reached max points
                if (!keepPlaying) {
                    currentMatch.setMatchStatus(MatchStatus.GameOver);
                }
            }

            //updates the score statistics of all users when game is over
            if (currentMatch.getMatchStatus().equals(MatchStatus.GameOver)){
                userService.incrementPlayedGames(userId);
                userService.updateOverallWins(userId, matchId);
            }
        }catch(IncorrectIdException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }


    //get matchStatus
    @GetMapping("/matches/{matchId}/status") // tested: ok, failed ok
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<MatchStatus> getMatchStatus(@PathVariable long matchId) {
        try{
            Match currentMatch = gameManager.getMatch(matchId);
            return ResponseEntity.ok(currentMatch.getMatchStatus());
        }catch (IncorrectIdException e) {
            return ResponseEntity.ok(MatchStatus.NotYetCreated);
        }
    }
/*
    //testendpoint to get chosencard to see if chosen card is even saved in hand
    @GetMapping("/matches/{matchId}/chosencard/{userId}") // NOT TESTED
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<WhiteCard> getChosenCard(@PathVariable long matchId, @PathVariable long userId) throws Exception {
        Match currentMatch = gameManager.getMatch(matchId);
        Round currentRound = currentMatch.getRound();
        Hand playerHand = currentRound.getHandByUserId(userId);
        WhiteCard chosenCard = playerHand.getChosenCard();

        return ResponseEntity.ok(chosenCard);
    }*/

    //----------------------MOVED FROM GAMECONTROLLER---------------------------------------------------
    //Creates a new match and puts all players from the lobby into  it
    //Should delete the lobby
    @PostMapping("/matches/{lobbyId}") // tested: ok, fail
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatchGetDTO startingMatch(@PathVariable long lobbyId){
        String baseErrorMessage1 = "Match could not be created";
        try {
            Match newMatch = gameService.startMatch(lobbyId); // incorrectIdException: returns new match
            ArrayList<User> currentPlayers = newMatch.getMatchPlayers();
            userService.setSuperVotes(currentPlayers, newMatch.getAvailable_Supervotes());

            return DTOMapper.INSTANCE.convertEntityToMatchGetDTO(newMatch);
        }
        catch (IncorrectIdException e1){
            throw new ResponseStatusException(HttpStatus.CONFLICT, baseErrorMessage1);
        }
    }

    // tells server a supervote was cast and laugher should be played for all, decreases available supervote by 1
    @PutMapping("/matches/{matchId}/supervotes/{userId}") // tested: ok, fail
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Boolean> getPlayLaughter(@PathVariable long matchId, @PathVariable long userId) throws IncorrectIdException {
        // update laugh-status in match
        Match currentMatch = gameManager.getMatch(matchId);
        currentMatch.setLaughStatus(LaughStatus.Laughing);

        //update available supervotes for player in match
        currentMatch.decreaseSuperVote(userId);

        // update available supervote for player in database
        userService.updateSupervote(userId); // bad request: "You used up your supervotes!"
        return ResponseEntity.ok(true);
    }

    //get laugh status .
    @GetMapping("/matches/{matchId}/laughStatus") // tested: ok, fail
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<LaughStatus> getMatchLaughStatus(@PathVariable long matchId) throws IncorrectIdException {
        try{
            Match currentMatch = gameManager.getMatch(matchId);
            return ResponseEntity.ok(currentMatch.getLaughStatus());
        }catch(IncorrectIdException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    // only turn laughstatus to "silence" once everyone hear a laughter
    @PutMapping("/matches/{matchId}/laugh")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void countLaughs(@PathVariable long matchId) throws IncorrectIdException {
        try{
            Match currentMatch = gameManager.getMatch(matchId);
            currentMatch.updateLaughStatus();
        }catch(IncorrectIdException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }


    // delete lobby only once even if several requests
    @DeleteMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteLobby(@PathVariable long lobbyId) throws Exception {
        gameService.deleteLobby(lobbyId);
    }
}