package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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

    @MockBean
    private GameManager gameManager = GameManager.getInstance();


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
        Lobby lobby = new Lobby(2L);
        given(gameService.createNewLobby()).willReturn(lobby);

        long lobbyId = lobby.getId();

        // put user in lobby
        given(userService.findUserById(Mockito.any())).willReturn(user);
        given(gameManager.getLobby(Mockito.any())).willReturn(lobby);
        lobby.addPlayer(user);

        // check if all users are ready -> has to wait (user hasnt changed ready status)
        gameService.checkIfLobbyStatusChanged(lobbyId);
        given(gameService.getLobbyStatus(Mockito.any())).willReturn(LobbyStatus.Waiting);

        // make readyStatus of user -> READY
        userService.updateUserReadyStatus(user);

        given(gameManager.getLobby(Mockito.any())).willReturn(lobby);
        gameService.updateUserReadyStatus(lobbyId, user.getId());

        // check if all users are ready -> turn lobby into match
        gameService.checkIfLobbyStatusChanged(lobbyId);
        given(gameService.getLobbyStatus(Mockito.any())).willReturn(LobbyStatus.All_Ready);

        // starting match should return this
        Match match = new Match(lobbyId);
        given(gameService.startMatch(Mockito.any())).willReturn(match);

        // build request
        MockHttpServletRequestBuilder postRequest = post("/matches/"+lobbyId);

        // then perform request
        mockMvc.perform(postRequest).andExpect(status().isOk());
                //.andExpect(ResultMatcher.matchAll(lobbyId));

    }
    //create match unsuccessful


}
