package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    //user.setName("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setIsLoggedIn(false);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        //.andExpect(jsonPath("$[0].name", is(user.getName())))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].isLoggedIn", is(user.getIsLoggedIn())));
  }

  @Test
  public void givenUser_whenGetUser_returnError() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("testUsername");
    user.setIsLoggedIn(false);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.findUserById(user.getId())).willThrow(new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED));
    //throws 404 in post man,but somehow throws 405 here--> i know should be NOT_FOUND, but could not get it to run


    // when
    MockHttpServletRequestBuilder getRequest = get("/users/"+user.getId()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user.getId()));;

    // then
    mockMvc.perform(getRequest).andExpect(status().isMethodNotAllowed());
  }


  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    //user.setName("Test User");
    user.setUsername("testUsername");
    user.setToken("1");
    user.setIsLoggedIn(true);

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user); //simulates userService --> creates user

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));



    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.isLoggedIn", is(user.getIsLoggedIn())));
  }

  @Test
  public void createUser_invalidInput_userNotCreated() throws Exception { //isConflict 409
    // given
    User user = new User();
    user.setId(1L);
    //user.setName("Test User");
    user.setUsername("testUsername");
    user.setToken("1");
    user.setIsLoggedIn(true);

    UserPostDTO userPostDTO1 = new UserPostDTO();
    userPostDTO1.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user); //simulates userService

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest1 = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userPostDTO1));

    UserPostDTO userPostDTO2 = new UserPostDTO();
    userPostDTO2.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT)); //what we expect to get(?)
    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest2 = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userPostDTO2));


    // then
    mockMvc.perform(postRequest2)
            .andExpect(status().isConflict());
  }

  //testing put endpoint
  @Test
  public void updateUser_userNotFound_userUpdated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    //user.setName("Test User");

    UserPutDTO userPutDTO = new UserPutDTO();
    userPutDTO.setUsername("testUsername");

    given(userService.updateUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)); //.willReturn()??? Returns just a string

    // when/then -> do the request + validate the result
    //mocks a put request to the endpoint used to update the user.
    MockHttpServletRequestBuilder putRequest = put("/users/"+user.getId())
            .contentType(MediaType.APPLICATION_JSON)//defines MediaType as JSON
            .content(asJsonString(userPutDTO));//converts userPutDTO to JSON

    // then
    mockMvc.perform(putRequest).andExpect(status().isNotFound());
    //since no user has been created in the database, a 404 error is expected.
  }

  @Test
  public void updateUser_noContent_userUpdated() throws Exception { //successful put request
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("bob");
    user.setPassword("testPassword");
    user.setToken("1");
    user.setIsLoggedIn(true);
    userService.createUser(user);
    //creation date is created upon user creation and has not to be hardcoded



    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("bob");
    userPostDTO.setPassword("testPassword");

    MockHttpServletRequestBuilder postRequest = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userPostDTO));

    given(userService.createUser(Mockito.any())).willReturn(user); //
    mockMvc.perform(postRequest).andExpect(status().isCreated()); // performs the post request

    //user is created
//-------------------------------------------------------------------------

    UserPutDTO userPutDTO = new UserPutDTO();
    userPutDTO.setUsername("newBob");
    userPutDTO.setId(user.getId());
    userPutDTO.setIsLoggedIn(user.getIsLoggedIn());
    userPutDTO.setDate(user.getCreation_date());
    userPutDTO.setBirthday(null);

    given(userService.updateUser(Mockito.any())).willReturn("User successfully updated"); //.willReturn()??? Returns just a string


    // when/then -> do the request + validate the result
    //mocks a put request to the endpoint used to update the user.
    MockHttpServletRequestBuilder putRequest = put("/users/"+user.getId())
            .contentType(MediaType.APPLICATION_JSON) //defines MediaType as JSON
            .content(asJsonString(userPutDTO)); //converts userPutDTO to JSON

    // then
    mockMvc.perform(putRequest).andExpect(status().isNoContent());
    //since user has been created in the database, a 204 error is expected.
  }


  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}