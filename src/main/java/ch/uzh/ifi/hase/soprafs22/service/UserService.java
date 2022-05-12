package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  public UserRepository getUserRepository(){return this.userRepository;}
  GameManager gameManager = GameManager.getInstance();

  @Autowired //what does @Autowired do exactly?
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());

    //sets date and time to the moment it gets created
    Date date = new Date();
    newUser.setCreation_date(date);

    checkIfUserExists(newUser);

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();



    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
      User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
      //User userByName = userRepository.findByName(userToBeCreated.getName());

      String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
      if (userByUsername != null) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  String.format(baseErrorMessage, "username ", "is"));
      }
  }

  private void checkIfUsernameUnique(String username){
      List<User> allUsers=getUsers();
      for(User user:allUsers){
          if(user.getUsername().equals(username)){
              String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                      String.format(baseErrorMessage, "username ", "is"));
          }
      }
  }


  public void logOutUser(User userToLogOut){
    userToLogOut.setUserStatus(UserStatus.OFFLINE);
    userToLogOut.setIsReady(ReadyStatus.UNREADY);
    userRepository.flush();
    log.debug("Logged out user %s", userToLogOut.getUsername());
  }

    public void logInUser(User userToLogIn){
        //userToLogIn.setToken(UUID.randomUUID().toString());
        userToLogIn.setUserStatus(UserStatus.ONLINE);
        userToLogIn.setIsReady(ReadyStatus.UNREADY);
        userToLogIn.setScore(0);
        userRepository.flush();
        log.debug("Logged in user %s", userToLogIn.getUsername());
    }

  //searches for the requested user in the database
  public User findUserData(long id) {
    User requestedUser = userRepository.findById(id);
    if(requestedUser != null){
        return requestedUser;
    }
    else{
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  public User findUserByToken(String token){
    User requestedUser = userRepository.findByToken(token);
    return requestedUser;
  }

  public User findUserById(long id) {
    User requestedUser = userRepository.findById(id);
    if (requestedUser==null){
        String baseErrorMessage = "User not found!";
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(baseErrorMessage));
    }
    return requestedUser;
  }

  public User findUserByUsername(String username){
      User requestedUser = userRepository.findByUsername(username);
      return requestedUser;
  }

  public void updateUserUsername(UserPutDTO userPutDTO){
      User databaseUser=findUserById(userPutDTO.getId());
      if (userPutDTO.getUsername()!= null && userPutDTO.getUsername().trim().length() > 0) { //if a username gets entered in the frontend and it is not an empty space
          if (databaseUser != null) { //if it finds the user corresponding to the userid
              checkIfUsernameUnique(userPutDTO.getUsername()); //if username is unique
              databaseUser.setUsername(userPutDTO.getUsername()); //change username
          }
      }
  }

  public void updateUserPassword(UserPutDTO userPutDTO){
      User databaseUser=findUserById(userPutDTO.getId()); //user in database for id
      if(userPutDTO.getPassword()!=null){
          databaseUser.setPassword(userPutDTO.getPassword());
      }
    }

    public ReadyStatus updateUserReadyStatus(User user){
        User databaseUser=findUserById(user.getId()); //user in database for id
        if(user.getIsReady()!=null){
            if(user.getIsReady().equals(ReadyStatus.READY)){
                databaseUser.setIsReady(ReadyStatus.UNREADY);

            }
            else{
                databaseUser.setIsReady(ReadyStatus.READY);
            }
            return user.getIsReady();
        }
        return user.getIsReady();
    }

    public void resetUserReadyStatus(User user){
        User databaseUser=findUserById(user.getId()); //user in database for id
        databaseUser.setIsReady(ReadyStatus.UNREADY);
    }

    //sets the score of a user back to 0
    public void resetUserScore(User user){
        User databaseUser = findUserById(user.getId()); //user in database for id
        databaseUser.setScore(0);
    }



  public String updateCustomWhiteText(UserPutDTO userPutDTO){
      User dbUser = findUserById(userPutDTO.getId());
      if (dbUser != null){
          dbUser.setCustomWhiteText(userPutDTO.getCustomWhiteText());
          return userPutDTO.getCustomWhiteText();
      }
      else{
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "Couldn't set your custom text");
      }
  }

  public String updateUser(UserPutDTO userPutDTO) {
      updateUserUsername(userPutDTO);
      updateUserPassword(userPutDTO);
      //updateUserReadyStatus(userPutDTO);
      return "User successfully updated";
  }

  /*
    public LobbyStatus getLobbyStatus() {
        List<User> users = getUsers();
        for (User user : users){
            if (user.getIsReady()== ReadyStatus.UNREADY){
                return LobbyStatus.Waiting;
            }
        }
        return LobbyStatus.All_Ready;
  }
  */
    //----------------parts which were in gameservice before and got moved--------------------

    public List<User> test_getUsers() {
        return this.userRepository.findAll();
    }


    //updates scores of all currentRound winners in database
    public void updateScores(ArrayList<WhiteCard> highestScoreCards){

        for (WhiteCard winnerCard: highestScoreCards){
            long id = winnerCard.getOwner().getId();
            User userToBeUpdated = findUserById(id);
            int oldScore = userToBeUpdated.getScore();
            userToBeUpdated.setScore(oldScore+1);
        }

    }

    public void resetMatchPlayers(long matchId) throws IncorrectIdException {
        Match endedMatch = gameManager.getMatch(matchId);
        // reset score and ready status for all players in match

        // reset score and ready status for all players in database
        ArrayList<User> currentPlayers = endedMatch.getMatchPlayers();
        for (User player : currentPlayers){
            long id = player.getId();
            User user = findUserById(id);
            user.setIsReady(ReadyStatus.UNREADY);
            user.setScore(0);

        }
    }


}