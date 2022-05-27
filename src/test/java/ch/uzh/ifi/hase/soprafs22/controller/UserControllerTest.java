package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.LaughStatus;
import ch.uzh.ifi.hase.soprafs22.constant.MatchStatus;
import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.*;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
public class  UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    public GameManager gameManager;

    @MockBean
    public UserRepository userRepository;

    @Test
    void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setUserStatus(UserStatus.OFFLINE);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].userStatus", is(user.getUserStatus().toString())));
    }

    @Test //with ID get userProfile -> complete
    void givenUserID_getUserProfile_thenReturnJsonArray() throws Exception {
        // create user and set ID
        User user = new User();
        user.setUsername("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setId(1L);

        // this mocks the UserService -> we define above what the userService should
        given(userService.findUserData(user.getId())).willReturn(user);

        // build GET request with user-specific id
        MockHttpServletRequestBuilder getRequest = get("/users/?id="+user.getId().toString());

        // performing request should return OK status
        MvcResult result = mockMvc.perform(getRequest).andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.token", is(user.getToken())))
                .andReturn();
    }


    @Test // getUserById -> complete
    void givenUserId_whenGetUser_returnError() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setUserStatus(UserStatus.OFFLINE);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.findUserData(user.getId())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/?id="+user.getId().toString());

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
    }

    @Test //with userToken get userProfile -> complete
    void givenUserToken_getUserProfile_thenReturnJsonArray() throws Exception {
        // create user and set ID
        User user = new User();
        user.setUsername("Firstname Lastname");
        user.setToken("abcdefg");
        user.setId(1L);

        // this mocks the UserService -> we define above what the userService should
        given(userService.findUserByToken(user.getToken())).willReturn(user);

        // build GET request with user-specific id
        MockHttpServletRequestBuilder getRequest = get("/users/"+user.getToken());

        // performing request should return OK status
        MvcResult result = mockMvc.perform(getRequest).andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.token", is(user.getToken())))
                .andReturn();
    }

    @Test // GET user by token -> complete
    void whenGetUserByToken_returnError() throws Exception {
        // create user and set ID
        User user = new User();
        user.setUsername("Firstname Lastname");
        user.setToken("abcdefg");
        user.setId(1L);

        // this mocks the UserService -> we define above what the userService should
        given(userService.findUserByToken(user.getToken())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // build GET request with user-specific id
        MockHttpServletRequestBuilder getRequest = get("/users/"+user.getToken());

        // performing request should return NOT FOUND status
        MvcResult result = mockMvc.perform(getRequest).andExpect(status()
                .isNotFound())
                .andReturn();
    }

    //@Disabled
    @Test // POST create user success -> complete
    void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("password");

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testUsername");
        user2.setToken("1");
        user2.setUserStatus(UserStatus.ONLINE);
        user2.setPassword("password");
        user2.setIsReady(ReadyStatus.UNREADY);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(user.getUsername());
        userPostDTO.setPassword(user.getPassword());

        given(userService.createUser(Mockito.any())).willReturn(user2); //simulates userService --> creates user
        doNothing().when(userService).logInUser(user2);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user2.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user2.getUsername())))
                .andExpect(jsonPath("$.userStatus", is(user2.getUserStatus().toString())))
                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
                .andReturn()
        ;


    }

    @Test // POST create user: username alrdy taken -> complete
    void createUser_invalidInput_userNotCreated() throws Exception { //isConflict 409
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setPassword("pw");
        user.setToken("1");
        user.setUserStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO1 = new UserPostDTO();
        userPostDTO1.setUsername("testUsername");
        userPostDTO1.setPassword(user.getPassword());

        given(userService.createUser(Mockito.any())).willReturn(user); //simulates userService

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

    @Test // POST log-in user success -> complete
    void logInUser_success() throws Exception {
        // given
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.UNREADY);
        user.setUserStatus(UserStatus.ONLINE);
        user.setId(1L);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(user.getUsername());
        userPostDTO.setPassword(user.getPassword());

        List<User> allUsers = Collections.singletonList(user);
        given(userService.getUsers()).willReturn(allUsers);

        //given(userRepository.save(user)).willReturn(user);
        doNothing().when(userService).logInUser(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
                .andExpect(jsonPath("$.userStatus", is(UserStatus.ONLINE.toString())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test // POST log-in user no sucess -> complete
    void logInUser_unsuccessful() throws Exception {
        // given
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.UNREADY);
        user.setUserStatus(UserStatus.ONLINE);
        user.setId(1L);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(user.getUsername());
        userPostDTO.setPassword("smth else");

        List<User> allUsers = Collections.singletonList(user);
        given(userService.getUsers()).willReturn(allUsers);

        //given(userRepository.save(user)).willReturn(user);
        doNothing().when(userService).logInUser(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print()). andReturn();

        assertTrue(result.getResolvedException().getMessage().contains("The Password or Username provided is wrong!")); //

    }

    @Test // PUT update username/pw success -> complete
    void updateUser_noContent_userUpdated() throws Exception { //successful put request
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setUserStatus(UserStatus.OFFLINE);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newName");

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.findUserById(user.getId())).willReturn(user);
        given(userService.updateUser(userPutDTO)).willReturn("User successfully updated");

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/"+user.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isNoContent());


    }


    @Test // PUT change username/pw fail -> complete.
    void updateUser_userNotFound_userUpdated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("username");

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("testUsername");

        given(userService.updateUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)); //.willReturn()??? Returns just a string

        // when/then -> do the request + validate the result
        //mocks a put request to the endpoint used to update the user.
        MockHttpServletRequestBuilder putRequest = put("/users/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON)//defines MediaType as JSON
                .content(asJsonString(userPutDTO));//converts userPutDTO to JSON

        // then
        MvcResult result = mockMvc.perform(putRequest).andExpect(status().isNotFound()).andReturn();
        //since no user has been created in the database, a 404 error is expected.
    }

    @Test // PUT log-out user success -> complete
    void logoutUser_success() throws Exception { //successful put request
        // given
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.UNREADY);
        user.setUserStatus(UserStatus.OFFLINE);
        user.setToken("1");
        user.setId(1L);

        //List<User> allUsers = Collections.singletonList(user);
        given(userService.findUserByToken(user.getToken())).willReturn(user);
        doNothing().when(userService).logOutUser(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/logout/?token="+user.getToken());

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test // PUT log-out user no success -> complete
    void logoutUser_no_success() throws Exception { //successful put request
        // given
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.UNREADY);
        user.setUserStatus(UserStatus.OFFLINE);
        user.setToken("1");
        user.setId(1L);

        //List<User> allUsers = Collections.singletonList(user);
        given(userService.findUserByToken(user.getToken())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        doNothing().when(userService).logOutUser(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/logout/?token="+user.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                ;

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test // POST add user to lobby with lobbyId and userId -> complete
    void addUser_to_lobbyList_success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.UNREADY);
        user.setUserStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setId(2L);

        Lobby lobby = new Lobby(1L);
        lobby.addPlayer(user);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        given(userService.findUserById(user.getId())).willReturn(user);

        doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        doNothing().when(gameService).addPlayerToLobby(lobby.getId(), user);

        given(gameManager.getLobby(Mockito.anyLong())).willReturn(lobby);
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/lists/players/2");

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].userStatus", is(user.getUserStatus().toString())))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test // POST add user to lobby with lobbyId and userId NOT SUCCESSFUL -> complete
    void addUser_to_lobbyList_no_success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.UNREADY);
        user.setUserStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setId(2L);

        Lobby lobby = new Lobby(1L);
        lobby.addPlayer(user);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        given(userService.findUserById(user.getId())).willReturn(user);

        doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        doNothing().when(gameService).addPlayerToLobby(lobby.getId(), user);

        given(gameManager.getLobby(Mockito.anyLong())).willThrow(new IncorrectIdException("The lobby was not found"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/lists/players/2");

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andExpect(status().isConflict()) //
                .andDo(MockMvcResultHandlers.print()).andReturn();

        assertTrue(result.getResolvedException().getMessage().contains("No lobby with this id could be found.")); // MvcResult result = .andReturn()

    }

    @Test // PUT create custom card SUCCESS -> complete
    void createCustomCard_success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setId(2L);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername(user.getUsername());
        userPutDTO.setId(2L);
        userPutDTO.setCustomWhiteText("My own card");

        Lobby lobby = new Lobby(1L);
        lobby.addPlayer(user);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        given(userService.updateCustomWhiteText(Mockito.any())).willReturn(userPutDTO.getCustomWhiteText());
        given(gameService.updateCustomText(lobby.getId(), user.getId(), userPutDTO)).willReturn("Successfully updated customText in Lobby through gameManager");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+lobby.getId()+"/white-cards/"+user.getId()+"/custom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(userPutDTO.getCustomWhiteText(), result.getResponse().getContentAsString());
    }

    @Test // PUT create custom card no success -> complete (check for conflic error as well)
    void createCustomCard_noSuccess() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setId(2L);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername(user.getUsername());
        userPutDTO.setId(2L);
        userPutDTO.setCustomWhiteText("My own card");

        given(gameService.updateCustomText(1L, user.getId(), userPutDTO)).willReturn("Successfully updated customText in Lobby through gameManager");
        given(userService.updateCustomWhiteText(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+"0"+"/white-cards/"+user.getId()+"/custom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test // GET hand by userId success -> complete
    void getHand_Success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setId(2L);
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        ArrayList<WhiteCard> hand = new ArrayList<>();

        int numberOfCards = 0;
        while (numberOfCards < 10){
            WhiteCard newCard = new WhiteCard(user);
            newCard.setText(numberOfCards+"th card");
            hand.add(newCard);
            numberOfCards++;
        }

        Hand userHand = new Hand(user);
        userHand.setUserHand(hand);

        Match match = new Match(1L);
        match.setMatchPlayers(users);
        match.createRound();
        Round round = match.getRound();
        Hand hand1 = round.getHandByUserId(user.getId());
        hand1.setUserHand(hand);
        given(gameManager.getMatch(match.getId())).willReturn(match);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/hands/"+user.getId());


        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].text", is("0th card")))
                .andExpect(jsonPath("$[5].text", is("5th card")))
                .andExpect(jsonPath("$[9].text", is("9th card")))
                .andReturn();
    }

    @Test // GET hand by userId no success -> complete
    void getHand_noSuccess() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setId(2L);
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        ArrayList<WhiteCard> hand = new ArrayList<>();

        int numberOfCards = 0;
        while (numberOfCards < 10){
            WhiteCard newCard = new WhiteCard(user);
            newCard.setText(numberOfCards+"th card");
            hand.add(newCard);
            numberOfCards++;
        }

        Hand userHand = new Hand(user);
        userHand.setUserHand(hand);

        Match match = new Match(1L);
        given(gameManager.getMatch(match.getId())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/hands/"+user.getId());


        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test // PUT flip ready status -> complete
    void flipReadyStatus_Success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);

        given(userService.findUserById(user.getId())).willReturn(user);
        given(userService.updateUserReadyStatus(user)).willReturn(ReadyStatus.UNREADY);
        given(gameService.updateUserReadyStatus(0L, 2L)).willReturn("Successfully updated readyStatus in Lobby through gameManager");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+0L+"/users/"+user.getId());


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test // PUT flip ready status no success -> complete
    void flipReadyStatus_noSuccess() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);

        given(userService.findUserById(user.getId())).willReturn(user);
        given(userService.updateUserReadyStatus(user)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        given(gameService.updateUserReadyStatus(0L, 2L)).willReturn("Successfully updated readyStatus in Lobby through gameManager");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+0L+"/users/"+user.getId());


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue(result.getResolvedException().getMessage().contains("No lobby with this id could be found.")); // MvcResult result = .andReturn()

    }

    @Test // PUT reset ready status  success -> complete
    void resetReadyStatus_Success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);

        given(userService.findUserById(user.getId())).willReturn(user);
        doNothing().when(userService).resetUserReadyStatus(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+0L+"/users/"+user.getId()+"/status");


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test // PUT reset ready status no success -> complete
    void resetReadyStatus_noSuccess() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);


        //doNothing().when(userService).resetUserReadyStatus(user);
        given(userService.findUserById(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        //doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+0L+"/users/"+user.getId()+"/status");


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test // PUT reset score  success -> complete
    void resetScore_Success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);


        //doNothing().when(userService).resetUserReadyStatus(user);
        given(userService.findUserById(Mockito.anyLong())).willReturn(user);
        doNothing().when(userService).resetUserScore(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/users/"+user.getId()+"/scores");


        // then
        MvcResult result = mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent()).andReturn();

    }

    @Test // PUT reset score no success -> complete
    void resetScore_noSuccess() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);

        given(userService.findUserById(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        doNothing().when(userService).resetUserScore(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/users/"+user.getId()+"/scores");

        // then
        MvcResult result = mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test // GET matchStatus success -> complete
    void getMatchStatus_seeIfStartNewRoundOrEndGame_Success() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);

        given(gameManager.getMatch(match.getId())).willReturn(match);
        doNothing().when(gameManager).startNewRound(match.getId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/rounds");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("\"MatchOngoing\"", result.getResponse().getContentAsString());

        match.setMatchStatus(MatchStatus.GameOver);
        // then
        MvcResult result2 = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("\"GameOver\"", result2.getResponse().getContentAsString());
    }

    @Test // GET matchStatus no success -> complete
    void getMatchStatus_seeIfStartNewRoundOrEndGame_noSuccess() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);

        given(gameManager.getMatch(match.getId())).willThrow(new IncorrectIdException("The match was not found"));
        doNothing().when(gameManager).startNewRound(match.getId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/rounds");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test // PUT start next round/end game -> complete
    void startNextRound_and_EndGameStatus_Success() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.createRound();
        Round round = match.getRound();
        round.resetStartRoundStatus();

        ArrayList<WhiteCard> winnerCards = new ArrayList<>();
        WhiteCard c = new WhiteCard();
        c.setText("winnerCard");
        winnerCards.add(c);

        given(gameManager.getMatch(match.getId())).willReturn(match);
        doNothing().when(userService).updateScores(Mockito.any());
        given(gameManager.evaluateNewRoundStart(match.getId())).willReturn(true); // true= open next round

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+match.getId()+"/scores/1");

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andReturn();

        User user = new User();
        user.setId(1L);
        match.setMatchStatus(MatchStatus.GameOver);
        doNothing().when(userService).incrementPlayedGames(Mockito.anyLong());
        doNothing().when(userService).updateOverallWins(user.getId(), match.getId()); //<----------
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest2 = put("/matches/"+match.getId()+"/scores/1");

        // then
        MvcResult result2 = mockMvc.perform(putRequest2)
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test // PUT start next round/end game -> complete
    void startNextRound_and_EndGameStatus_noSuccess() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.createRound();
        Round round = match.getRound();
        round.resetStartRoundStatus();

        ArrayList<WhiteCard> winnerCards = new ArrayList<>();
        WhiteCard c = new WhiteCard();
        c.setText("winnerCard");
        winnerCards.add(c);

        given(gameManager.getMatch(match.getId())).willThrow(new IncorrectIdException("The match was not found"));
        doNothing().when(userService).updateScores(Mockito.any());
        given(gameManager.evaluateNewRoundStart(match.getId())).willReturn(true); // true= open next round

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+match.getId()+"/scores/1");

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test // GET match status success -> complete
    void getMatchStatus_Success() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);

        given(gameManager.getMatch(match.getId())).willReturn(match);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/status");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("\"MatchOngoing\"", result.getResponse().getContentAsString());

        match.setMatchStatus(MatchStatus.GameOver);
        // then
        MvcResult result2 = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("\"GameOver\"", result2.getResponse().getContentAsString());
    }

    @Test // GET match status success -> complete
    void getMatchStatus_noSuccess() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);

        given(gameManager.getMatch(match.getId())).willThrow(new IncorrectIdException("The match was not found"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/status");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("NotYetCreated")))
                .andReturn();
    }

    @Test // POST create new match success -> not complete
    void createNewMatch_matchExists() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        user.setUsername("anna");
        user.setIsReady(ReadyStatus.READY);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Lobby lobby = new Lobby(0L);
        Match match = new Match(lobby.getId());
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.setMatchPlayers(users);
        match.setAvailable_Supervotes(1);

        given(gameService.startMatch(lobby.getId())).willReturn(match);
        doNothing().when(userService).setSuperVotes(users, match.getAvailable_Supervotes());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/matches/"+lobby.getId());

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(match.getId().intValue())))
                .andExpect(jsonPath("$.matchStatus", is(MatchStatus.MatchOngoing.toString())))
                .andExpect(jsonPath("$.matchPlayers[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$.matchPlayers[0].id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.matchPlayers[0].isReady", is(user.getIsReady().toString())))
                .andReturn();

    }

    @Test // POST create new match no success -> complete
    void createNewMatch_matchDoesntExist() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        user.setUsername("anna");
        user.setIsReady(ReadyStatus.READY);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Lobby lobby = new Lobby(0L);
        Match match = new Match(lobby.getId());
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.setMatchPlayers(users);
        match.setAvailable_Supervotes(1);

        given(gameService.startMatch(lobby.getId())).willThrow(new IncorrectIdException(""));
        doNothing().when(userService).setSuperVotes(users, match.getAvailable_Supervotes());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/matches/"+lobby.getId());

        // then
        MvcResult result = mockMvc.perform(postRequest)
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test // PUT cast supervote, decrease supervote by 1 success -> complete
    void castSuperVote_Success() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        user.setUsername("anna");
        user.setIsReady(ReadyStatus.READY);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Lobby lobby = new Lobby(0L);
        Match match = new Match(lobby.getId());
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.setMatchPlayers(users);
        match.setAvailable_Supervotes(1);
        match.setLaughStatus(LaughStatus.Laughing);

        given(gameManager.getMatch(match.getId())).willReturn(match);
        doNothing().when(userService).updateSupervote(user.getId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+match.getId()+"/supervotes/"+user.getId());

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)))
                .andReturn();
    }

    @Test // PUT change LaugingStatus with supervote no success -> no complete
    void castSuperVote_noSuccess() throws Exception {
        // given
        User user = new User();
        user.setId(2L);
        user.setUsername("anna");
        user.setIsReady(ReadyStatus.READY);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        Lobby lobby = new Lobby(0L);
        Match match = new Match(lobby.getId());
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.setMatchPlayers(users);
        match.setAvailable_Supervotes(1);
        match.setLaughStatus(LaughStatus.Laughing);

        given(gameManager.getMatch(match.getId())).willReturn(match);
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "You used up your supervotes!")).when(userService).updateSupervote(user.getId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+match.getId()+"/supervotes/"+user.getId());

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(result.getResolvedException().getMessage().contains("You used up your supervotes!"));
    }

    @Test // GET laugh status success -> complete
    void getLaughStatus_Success() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.setLaughStatus(LaughStatus.Laughing);

        given(gameManager.getMatch(match.getId())).willReturn(match);
        //doNothing().when(userService).updateScores(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/laughStatus");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Laughing")))
                .andReturn();


        match.setLaughStatus(LaughStatus.Silence);
        // then
        MvcResult result2 = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Silence")))
                .andReturn();

    }

    @Test // GET laugh status no success -> complete
    void getLaughStatus_noSuccess() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.setLaughStatus(LaughStatus.Laughing);

        given(gameManager.getMatch(match.getId())).willThrow(new IncorrectIdException("The match was not found"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/laughStatus");

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test // PUT increase laughCount success ->  complete
    void increase_laughCount_Success() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.setLaughStatus(LaughStatus.Laughing);

        given(gameManager.getMatch(match.getId())).willReturn(match);
        //doNothing().when(userService).updateScores(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+match.getId()+"/laugh");

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isOk()).andReturn();

    }

    @Test // PUT increase laughCount no success -> complete
    void increase_laughCount_noSuccess() throws Exception {
        // given
        Match match = new Match(0L);
        match.setMatchStatus(MatchStatus.MatchOngoing);
        match.setLaughStatus(LaughStatus.Laughing);

        given(gameManager.getMatch(match.getId())).willThrow(new IncorrectIdException("The match was not found"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+match.getId()+"/laugh");

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test // PUT increase laughCount no success ->  complete
    void deleteLobby_Success() throws Exception {
        // given
        Lobby lobby = new Lobby(0L);

        doNothing().when(gameService).deleteLobby(lobby.getId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/"+lobby.getId());

        // then
        MvcResult result = mockMvc.perform(deleteRequest)
                .andExpect(status().isOk())
                .andReturn();

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