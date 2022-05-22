package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.Round;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

   /* @MockBean
    private UserService userService;
*/

    @MockBean
    public GameManager gameManager;



 /*   @AfterEach
    void tearDown() {
        GameManager.resetGameManager();
    }
*/
    @Test
    public void getRules_success() throws Exception{

        //this won't return anything, since the gameService is mocked
        //ArrayList<String> expectedArrayList = gameService.getRulesFromText();

        ArrayList<String> expectedArrayList = new ArrayList<>();
        expectedArrayList.add("There  are two types of cards: Black cards and white cards.");



        given(gameService.getRulesFromText()).willReturn(expectedArrayList);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/rules")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn();
        String expected = "[\"There  are two types of cards: Black cards and white cards.\"]";
        assertEquals(expected,result.getResponse().getContentAsString());
    }


    @Test
    public void getLobbyStatus_success() throws Exception {
        //given
        Lobby testLobby = new Lobby(0L);
        testLobby.setLobbyStatus(LobbyStatus.All_Ready);



        given(gameService.getLobbyStatus(testLobby.getId())).willReturn(testLobby.getLobbyStatus());


        MockHttpServletRequestBuilder getRequest = get("/lobbies/"+testLobby.getId()+"/status")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals("\"All_Ready\"", content);
    }


    @Test
    public void getLobbyStatus_lobby_not_found() throws Exception {


        //given defines what the mocked class should return when calling a specific method
        given(gameService.getLobbyStatus(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT,"No lobby with this id could be found."));


        MockHttpServletRequestBuilder getRequest = get("/lobbies/"+anyLong()+"/status")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isConflict());
    }

    @Test
     void createNewLobby_returns_ok() throws Exception {

        //
        Lobby testLobby = new Lobby(1L);
        //converts long to int, since ResponseEntity seems to convert long to int
        int expected = Math.toIntExact(testLobby.getId());


        given(gameService.createNewLobby()).willReturn(testLobby);


        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expected)));

    }



    //ADD assertions
    @Test
     void getAllLobbies_returns_ok() throws Exception {

        //given
        Lobby testLobby = new Lobby(0L);
        Lobby testLobby2 = new Lobby(1L);
        ArrayList<Lobby> lobbies = new ArrayList<>();
        lobbies.add(testLobby);
        lobbies.add(testLobby2);


        given(gameManager.getAllLobby()).willReturn(lobbies);

        MockHttpServletRequestBuilder getRequest = get("/lobbies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

    }

//DOESN'T WORK YET
    @Test
    void getAllUsersByLobbyId_success() throws Exception {
        //given
        Lobby testLobby = new Lobby(0L);
        testLobby.setId(0L);
        User user1 = new User();
        User user2 = new User();
        user1.setId(0L);
        user2.setId(1L);
        testLobby.addPlayer(user1);
        testLobby.addPlayer(user2);
        ArrayList<User> allTestUsers = new ArrayList<>();
        allTestUsers.add(user1);
        allTestUsers.add(user2);


        given(gameManager.getLobby(0L)).willReturn(testLobby);
        given(gameManager.getLobby(0L).getCurrentPlayers()).willReturn(allTestUsers);


        MockHttpServletRequestBuilder getRequest = get("/lobbies/"+testLobby.getId()+"/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk());
    }

    @Test
    void getRoundNumberSpecificMatch_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();
        Round testRound = testMatch.getRound();
        int roundNumber = testRound.getRoundNumber();

        given(gameManager.getMatch(0L)).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/"+testMatch.getId()+"/roundnumbers")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk());


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
                .andExpect(jsonPath("$.id", is(match.getId().intValue())))
                .andExpect(jsonPath("$.laughStatus", is(match.getLaughStatus())))
                .andExpect(jsonPath("$.available_Supervotes", is(match.getAvailable_Supervotes())));
        //@Mapping(source = "laughStatus", target = "laughStatus")
        //    @Mapping(source = "available_Supervotes", target = "available_Supervotes")

    }
*/
}
