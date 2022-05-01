package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    //@MockBean -> uncommenting this causes tests to fail
    //private GameManager gameManager = GameManager.getInstance();
    public GameManager gameManager = GameManager.getInstance();


    @Test
    public void getRules_success() throws Exception{
        ArrayList<String> expectedArrayList = gameService.getRulesFromText();
        given(gameService.getRulesFromText()).willReturn(expectedArrayList);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/rules")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }


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
/*
    // put user in lobby
        given(userService.findUserById(Mockito.anyLong())).willReturn(user);

        gameManager.createLobby();
        given(gameManager.getLobby(Mockito.anyLong())).willReturn(lobby); // !!!!!!!!!!

        lobby.addPlayer(user);
        ///given(lobby.addPlayer(Mockito.any(Object<User>))).willReturn(1);


    // check if all users are ready -> has to wait (user hasnt changed ready status)
        given(gameService.checkIfLobbyStatusChanged(Mockito.anyLong())).willReturn(LobbyStatus.Waiting);
        given(gameService.getLobbyStatus(Mockito.anyLong())).willReturn(LobbyStatus.Waiting);

    // make readyStatus of user -> READY
        // update in database
        given(userService.updateUserReadyStatus(Mockito.any())).willReturn(ReadyStatus.READY);

    // update user ready status in lobby players array
        given(gameService.updateUserReadyStatus(Mockito.anyLong(), Mockito.anyLong())).willReturn("Successfully updated readyStatus in Lobby through gameManager");

    // check if all users are ready -> turn lobby into match
        given(gameService.checkIfLobbyStatusChanged(Mockito.anyLong())).willReturn(LobbyStatus.All_Ready);
        given(gameService.getLobbyStatus(Mockito.anyLong())).willReturn(LobbyStatus.All_Ready);
*/
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

    }

}
