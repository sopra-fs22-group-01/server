package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
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

  @PostMapping("/users") //creates a User object
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    createdUser.setStatus(UserStatus.ONLINE);
    //set creation date of the user
    //createdUser.setDate();


    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }


  //get or post?
  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void checkPassword(@RequestBody UserPostDTO userPostDTO) {
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
        userPostDTO.setStatus(UserStatus.ONLINE);
        userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        return;
      }
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, baseErrorMessage);
  }

}

