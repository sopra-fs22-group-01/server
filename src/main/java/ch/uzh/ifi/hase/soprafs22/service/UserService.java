package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
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

  public void logOutUser(User userToLogOut){
    userToLogOut.setUserStatus(UserStatus.OFFLINE);
    userRepository.flush();
    log.debug("Logged out user %s", userToLogOut.getUsername());
  }

  public void logInUser(User userToLogIn){
    //userToLogIn.setToken(UUID.randomUUID().toString());
    userToLogIn.setUserStatus(UserStatus.ONLINE);
    userToLogIn.setIsReady(ReadyStatus.UNREADY);
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
    return requestedUser;
  }

  public String updateUser(UserPutDTO userPutDTO) {
      List<User> users = getUsers();
      for(User user:users) {
          if (user.getId() == userPutDTO.getId()) {
              //updates username from user if username provided has length >0 and is not space
              if (userPutDTO.getUsername().length() > 0 && userPutDTO.getUsername().trim().length() > 0) {
                  user.setUsername(userPutDTO.getUsername());
              }
              if (userPutDTO.getBirthday() != null) {
                  user.setBirthday(userPutDTO.getBirthday());
              }
              if (userPutDTO.getIsReady() != null) {
                  user.setIsReady(userPutDTO.getIsReady());
              }
              return "User successfully updated";
          }
      }

      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
  }




  //search for user by id and change birthdate and or username
}
