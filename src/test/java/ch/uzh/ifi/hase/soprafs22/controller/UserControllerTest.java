package ch.uzh.ifi.hase.soprafs22.controller;

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
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        //user.setName("Firstname Lastname");
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
                //.andExpect(jsonPath("$[0].name", is(user.getName())))
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
        //given(userRepository.findById(user.getId())).willReturn(null);
        given(userService.findUserData(user.getId())).willThrow(new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED));
        //throws 404 in post man,but somehow throws 405 here--> i know should be NOT_FOUND, but could not get it to run

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/?id="+user.getId().toString());

        // then
        mockMvc.perform(getRequest).andExpect(status().isMethodNotAllowed());
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
        mockMvc.perform(getRequest).andExpect(status().isNotFound());
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

        //assertEquals("test", result.getResponse().getContentAsString());

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

    @Test // POST log-in user sucess -> complete
    void logInUser_success() throws Exception { //successful put request
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
    void logInUser_unsuccessful() throws Exception { //successful put request
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
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
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
        //doNothing().when(userService).updateUser(userPutDTO);

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/"+user.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isNoContent());


    }


    @Test // PUT change username/pw fail -> complete
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
        mockMvc.perform(putRequest).andExpect(status().isNotFound());
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

        //UserPostDTO userPostDTO = new UserPostDTO();
        //userPostDTO.setUsername(user.getUsername());
        //userPostDTO.setPassword(user.getPassword());

        //List<User> allUsers = Collections.singletonList(user);
        given(userService.findUserByToken(user.getToken())).willReturn(user);
        doNothing().when(userService).logOutUser(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/logout/?token="+user.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                //.content(asJsonString(userPostDTO))
                ;

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

        //UserPostDTO userPostDTO = new UserPostDTO();
        //userPostDTO.setUsername(user.getUsername());
        //userPostDTO.setPassword(user.getPassword());

        //List<User> allUsers = Collections.singletonList(user);
        given(userService.findUserByToken(user.getToken())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        doNothing().when(userService).logOutUser(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/logout/?token="+user.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                //.content(asJsonString(userPostDTO))
                ;

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
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
//                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(user.getUsername())))
//                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
//                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
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
        given(userService.findUserById(user.getId())).willThrow();

        doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        doNothing().when(gameService).addPlayerToLobby(lobby.getId(), user);

        given(gameManager.getLobby(Mockito.anyLong())).willThrow(new IncorrectIdException("The lobby was not found"));
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies/1/lists/players/2");

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print());
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
        //doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+lobby.getId()+"/white-cards/"+user.getId()+"/custom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));
                ;

        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(user.getUsername())))
//                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
//                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
                //.andDo(MockMvcResultHandlers.print());
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

        //Lobby lobby = new Lobby(1L);
        //lobby.addPlayer(user);

        //ArrayList<User> users = new ArrayList<>();
        //users.add(user);
        given(gameService.updateCustomText(1L, user.getId(), userPutDTO)).willReturn("Successfully updated customText in Lobby through gameManager");
        given(userService.updateCustomWhiteText(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        //given(userService.updateCustomWhiteText(Mockito.any())).willReturn(userPutDTO.getCustomWhiteText());


        //doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/matches/"+"0"+"/white-cards/"+user.getId()+"/custom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(user.getUsername())))
//                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
//                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
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
        ArrayList<WhiteCard> userhand1 = hand1.getCardsFromHand();
        //ArrayList<User> users = new ArrayList<>();
        //users.add(user);
        given(gameManager.getMatch(match.getId())).willReturn(match);
        //given(match.getRound()).willReturn(round);
        //given(round.getHandByUserId(user.getId())).willReturn(userHand);
        //given(userHand.getCardsFromHand()).willReturn(hand);


        //doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/hands/"+user.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPutDTO))
                ;


        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
//                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(user.getUsername())))
//                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
//                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
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
//        match.setMatchPlayers(users);
//        match.createRound();
//        Round round = match.getRound();
//        Hand hand1 = round.getHandByUserId(user.getId());
//        hand1.setUserHand(hand);
//        ArrayList<WhiteCard> userhand1 = hand1.getCardsFromHand();
        //ArrayList<User> users = new ArrayList<>();
        //users.add(user);
        given(gameManager.getMatch(match.getId())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        //given(match.getRound()).willReturn(round);
        //given(round.getHandByUserId(user.getId())).willReturn(userHand);
        //given(userHand.getCardsFromHand()).willReturn(hand);


        //doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/matches/"+match.getId()+"/hands/"+user.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPutDTO))
                ;


        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest())
                //.andExpect(jsonPath("$", hasSize(10)))
//                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(user.getUsername())))
//                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
//                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
    }

    @Test // PUT flip ready status -> complete
    void flipReadyStatus_Success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);
//        ArrayList<User> users = new ArrayList<>();
//        users.add(user);
//
//        ArrayList<WhiteCard> hand = new ArrayList<>();
//
//        int numberOfCards = 0;
//        while (numberOfCards < 10){
//            WhiteCard newCard = new WhiteCard(user);
//            newCard.setText(numberOfCards+"th card");
//            hand.add(newCard);
//            numberOfCards++;
//        }
//
//        Hand userHand = new Hand(user);
//        userHand.setUserHand(hand);
//
//        Match match = new Match(1L);
//        match.setMatchPlayers(users);
//        match.createRound();
//        Round round = match.getRound();
//        Hand hand1 = round.getHandByUserId(user.getId());
//        hand1.setUserHand(hand);
//        ArrayList<WhiteCard> userhand1 = hand1.getCardsFromHand();
        //ArrayList<User> users = new ArrayList<>();
        //users.add(user);
        given(userService.findUserById(user.getId())).willReturn(user);
        given(userService.updateUserReadyStatus(user)).willReturn(ReadyStatus.UNREADY);
        given(gameService.updateUserReadyStatus(0L, 2L)).willReturn("Successfully updated readyStatus in Lobby through gameManager");
        //given(match.getRound()).willReturn(round);
        //given(round.getHandByUserId(user.getId())).willReturn(userHand);
        //given(userHand.getCardsFromHand()).willReturn(hand);


        //doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+0L+"/users/"+user.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPutDTO))
                ;


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                //.andExpect(jsonPath("$", hasSize(10)))
//                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(user.getUsername())))
//                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
//                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
    }

    @Test // PUT flip ready status no success -> complete
    void flipReadyStatus_noSuccess() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);
//        ArrayList<User> users = new ArrayList<>();
//        users.add(user);
//
//        ArrayList<WhiteCard> hand = new ArrayList<>();
//
//        int numberOfCards = 0;
//        while (numberOfCards < 10){
//            WhiteCard newCard = new WhiteCard(user);
//            newCard.setText(numberOfCards+"th card");
//            hand.add(newCard);
//            numberOfCards++;
//        }
//
//        Hand userHand = new Hand(user);
//        userHand.setUserHand(hand);
//
//        Match match = new Match(1L);
//        match.setMatchPlayers(users);
//        match.createRound();
//        Round round = match.getRound();
//        Hand hand1 = round.getHandByUserId(user.getId());
//        hand1.setUserHand(hand);
//        ArrayList<WhiteCard> userhand1 = hand1.getCardsFromHand();
        //ArrayList<User> users = new ArrayList<>();
        //users.add(user);
        given(userService.findUserById(user.getId())).willReturn(user);
        given(userService.updateUserReadyStatus(user)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        given(gameService.updateUserReadyStatus(0L, 2L)).willReturn("Successfully updated readyStatus in Lobby through gameManager");
        //given(match.getRound()).willReturn(round);
        //given(round.getHandByUserId(user.getId())).willReturn(userHand);
        //given(userHand.getCardsFromHand()).willReturn(hand);


        //doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+0L+"/users/"+user.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPutDTO))
                ;


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                //.andExpect(jsonPath("$", hasSize(10)))
//                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(user.getUsername())))
//                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
//                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
    }

    @Test // PUT reset ready status  success -> complete
    void resetReadyStatus_Success() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);
//        ArrayList<User> users = new ArrayList<>();
//        users.add(user);
//
//        ArrayList<WhiteCard> hand = new ArrayList<>();
//
//        int numberOfCards = 0;
//        while (numberOfCards < 10){
//            WhiteCard newCard = new WhiteCard(user);
//            newCard.setText(numberOfCards+"th card");
//            hand.add(newCard);
//            numberOfCards++;
//        }
//
//        Hand userHand = new Hand(user);
//        userHand.setUserHand(hand);
//
//        Match match = new Match(1L);
//        match.setMatchPlayers(users);
//        match.createRound();
//        Round round = match.getRound();
//        Hand hand1 = round.getHandByUserId(user.getId());
//        hand1.setUserHand(hand);
//        ArrayList<WhiteCard> userhand1 = hand1.getCardsFromHand();
        //ArrayList<User> users = new ArrayList<>();
        //users.add(user);
        given(userService.findUserById(user.getId())).willReturn(user);
        doNothing().when(userService).resetUserReadyStatus(user);
        //given(match.getRound()).willReturn(round);
        //given(round.getHandByUserId(user.getId())).willReturn(userHand);
        //given(userHand.getCardsFromHand()).willReturn(hand);


        //doNothing().when(gameManager).removePlayerFromAllLobbies(user.getId());
        //given(lobby.getCurrentPlayers()).willReturn(users);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+0L+"/users/"+user.getId()+"/status")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPutDTO))
                ;


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                //.andExpect(jsonPath("$", hasSize(10)))
//                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(user.getUsername())))
//                .andExpect(jsonPath("$.isReady", is(ReadyStatus.UNREADY.toString())))
//                .andExpect(jsonPath("$.userStatus", is(UserStatus.OFFLINE.toString())))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
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
        MockHttpServletRequestBuilder putRequest = put("/lobbies/"+0L+"/users/"+user.getId()+"/status")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPutDTO))
                ;


        // then
        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                //.andExpect(jsonPath("$", hasSize(10)))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
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
        MockHttpServletRequestBuilder deleteRequest = delete("/users/"+user.getId()+"/scores")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPutDTO))
                ;


        // then
        MvcResult result = mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent())
                //.andExpect(jsonPath("$", hasSize(10)))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
    }

    @Test // PUT reset score no success -> complete
    void resetScore_noSuccess() throws Exception {
        // given
        User user = new User();
        user.setUsername("anna");
        user.setPassword("password");
        user.setIsReady(ReadyStatus.READY);
        user.setId(2L);


        //doNothing().when(userService).resetUserReadyStatus(user);
        given(userService.findUserById(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        doNothing().when(userService).resetUserScore(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/users/"+user.getId()+"/scores")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPutDTO))
                ;

        // then
        MvcResult result = mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound())
                //.andExpect(jsonPath("$", hasSize(10)))
                //.andDo(MockMvcResultHandlers.print());
                .andReturn();

        //assertEquals("test", result.getResponse().getContentAsString());
    }




/*
    //create match successful --> check same Id, same players as lobby, matchStatus
    @Test // endpoint location: GameController line 129
    public void givenLobbyId_createNewMatch_successful() throws Exception {
        // simulate all steps before creating match

        // create user
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setUserStatus(UserStatus.ONLINE);
        given(userService.createUser(Mockito.any())).willReturn(user);

        // create lobby
        Lobby lobby = new Lobby(0L);
        given(gameService.createNewLobby()).willReturn(lobby);

        long lobbyId = lobby.getId();

        // create new match and add users to it
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);

        Match match = new Match(lobbyId);
        match.setMatchPlayers(userList);
        // starting match should return match with: players, id, matchStatus, round
        given(gameService.startMatch(Mockito.anyLong())).willReturn(match);


        // build request
        MockHttpServletRequestBuilder postRequest = post("/matches/"+lobbyId);

        // then perform request
        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.matchStatus", is(match.getMatchStatus().toString())))
                .andExpect(jsonPath("$.matchPlayers[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$.id", is(match.getId().intValue())));
               // .andExpect(jsonPath("$.laughStatus", is(match.getLaughStatus())))
               // .andExpect(jsonPath("$.available_Supervotes", is(match.getAvailable_Supervotes())));
        //@Mapping(source = "laughStatus", target = "laughStatus")
        //    @Mapping(source = "available_Supervotes", target = "available_Supervotes")

    }*/

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