package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {
    private User testUser;
    private User testUser2;
    private User testUser3;
    private User testUser4;
    private User testUser5;
    private Lobby lobby;
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        this.testUser = new User();
        this.testUser2 = new User();
        this.testUser3 = new User();
        this.testUser4 = new User();
        this.testUser5 = new User();
        this.lobby = new Lobby(2L);
        this.gameManager = GameManager.getInstance();



    }

    @AfterEach
    void tearDown() {
        this.testUser = null;
        this.testUser2 = null;
        this.testUser3 = null;
        this.testUser4 = null;
        this.testUser5 = null;
        this.lobby = null;
        GameManager.resetGameManager();
    }

    @Test
    void checkIfAllReadyFalse() throws Exception {
        lobby.addPlayer(testUser);
        testUser.setIsReady(ReadyStatus.UNREADY);
        boolean actual = lobby.checkIfAllReady();
        assertFalse(actual);
    }

    @Test
    void checkIfAllReadyTrue() throws Exception {
        lobby.addPlayer(testUser);
        testUser.setIsReady(ReadyStatus.READY);
        boolean actual = lobby.checkIfAllReady();
        assertTrue(actual);
    }

    @Test
    void addPlayers() {
    }

    @Test
    void removePlayer() {
    }

    @Test
    void checkIfEnoughPlayersNotEnough() {
        boolean actual = lobby.checkIfEnoughPlayers();
        assertFalse(actual);
    }

    @Test
    void checkIfEnoughPlayersEnough() throws Exception {
        lobby.addPlayer(testUser);
        lobby.addPlayer(testUser2);
        lobby.addPlayer(testUser3);
        lobby.addPlayer(testUser4);
        lobby.addPlayer(testUser5);
        boolean actual = lobby.checkIfEnoughPlayers();
        assertTrue(actual);
    }


    @Test
    void setGamePlayers() throws Exception {
        ArrayList<User> expected = new ArrayList<>();
        expected.add(testUser);
        expected.add(testUser2);
        lobby.addPlayer(testUser);
        lobby.addPlayer(testUser2);
        lobby.setGamePlayers();
        Match match = gameManager.getMatch(2L);
        assertEquals(expected, match.getMatchPlayers());

    }


}