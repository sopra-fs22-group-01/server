package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.Round;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameManager gameManager;
    private User testUser;
    private User testUser2;
    private ArrayList<User> players;
    private Lobby testLobby;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.gameManager = GameManager.getInstance();
        this.testUser = new User();
        this.testUser2 = new User();
        this.players = new ArrayList<>();
        this.players.add(testUser);
        this.players.add(testUser2);
        this.testLobby = gameManager.createLobby();
    }
    @AfterEach
    void tearDown() {
        this.testUser = null;
        this.testUser2 = null;
        this.gameManager.resetGameManager();
        players.clear();
        this.testLobby = null;
        GameManager.resetGameManager();
    }

    @Test
    void getRulesFromText_success() throws Exception {

        ArrayList<String> testArray = gameService.getRulesFromText();
        String expected = "There  are two types of cards: Black cards and white cards.";
        assertEquals(expected,testArray.get(0) );

    }




    @Test
    void addPlayerToLobby_success() throws Exception {
        //before adding a player
        Lobby lobby = gameService.createNewLobby();
        long lobbyId = lobby.getId();
        int playerCountBefore = lobby.getCurrentPlayerCount();
        int expectedCountBefore = 0;
        //check if no players in lobby at beginning
        assertEquals(expectedCountBefore, playerCountBefore);

        //adding a player
        gameService.addPlayerToLobby(lobbyId,testUser);

        int expectedCountAfter = 1;
        int playerCountAfter = lobby.getCurrentPlayerCount();
        //check if player got added
        assertEquals(expectedCountAfter, playerCountAfter);
        lobby = null;
    }


    //remove function doesnt work yet

  /*  @Test
    void removePlayerFromLobby_success() throws Exception {
        //adding a player
        testUser.setId(1L);
        testLobby.addPlayer(testUser);

        //before adding a player
        int playerCountBeforeRemoval = testLobby.getCurrentPlayerCount();
        int expectedCountBeforeRemoval = 1;
        //check if no players in lobby at beginning
        assertEquals(expectedCountBeforeRemoval, playerCountBeforeRemoval);

        //removing a player
        testLobby.removePlayer(testUser);

        int expectedCountAfterRemoval = 0;
        int playerCountAfterRemoval = testLobby.getCurrentPlayerCount();
        //check if player got added
        assertEquals(expectedCountAfterRemoval, playerCountAfterRemoval);


    }*/

    @Test
    void checkIfMinimumNumberOfPlayers_returns_True() throws Exception {
        //adding a player
        Lobby someOtherLobby = gameService.createNewLobby();
        gameService.addPlayerToLobby(someOtherLobby.getId(),testUser);

        boolean actual = gameService.checkIfMinimumNumberOfPlayers(someOtherLobby.getId());

        assertTrue(actual);
    }

    @Test
    void checkIfMinimumNumberOfPlayers_returns_false() throws Exception {
        Lobby someOtherLobby = gameService.createNewLobby();
        long lobbyId = someOtherLobby.getId();
        boolean actual = gameService.checkIfMinimumNumberOfPlayers(lobbyId);

        assertFalse(actual);
    }

    @Test
    void checkIfAllPlayersReady_true() throws Exception {
        testUser.setId(1l);
        testUser2.setId(2l);
        Lobby otherTestLobby = gameService.createNewLobby();
        otherTestLobby.addPlayer(testUser);
        otherTestLobby.addPlayer(testUser2);
        testUser.setIsReady(ReadyStatus.UNREADY);
        testUser2.setIsReady(ReadyStatus.UNREADY);

        gameService.updateUserReadyStatus(otherTestLobby.getId(), testUser.getId());
        gameService.updateUserReadyStatus(otherTestLobby.getId(), testUser2.getId());
        boolean actual = gameService.checkIfAllPlayersReady(otherTestLobby.getId());

        assertTrue(actual);

    }

}
