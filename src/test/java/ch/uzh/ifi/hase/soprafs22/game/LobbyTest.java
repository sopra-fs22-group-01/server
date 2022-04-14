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
    private Lobby lobby;
    private ArrayList<User> players;
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        this.testUser = new User();
        this.testUser.setIsReady(ReadyStatus.READY);
        this.testUser2 = new User();
        this.testUser2.setIsReady(ReadyStatus.UNREADY);
        this.players = new ArrayList<>();
        this.players.add(testUser);
        this.players.add(testUser2);
        this.lobby = new Lobby(0L);
        this.gameManager = GameManager.getInstance();
    }

    @AfterEach
    void tearDown() {
        this.testUser = null;
        this.testUser2 = null;
        this.lobby = null;
        this.gameManager = null;
        players.clear();
        this.players = null;
    }

    @Test
    void checkIfAllReadyFalse() throws Exception {
        lobby.addPlayer(testUser);
        lobby.addPlayer(testUser2);
        boolean actual = lobby.checkIfAllReady();
        assertEquals(false, actual);
    }

    @Test
    void checkIfAllReadyTrue() throws Exception {
        testUser2.setIsReady(ReadyStatus.READY);
        lobby.addPlayer(testUser);
        lobby.addPlayer(testUser2);
        boolean actual = lobby.checkIfAllReady();
        assertEquals(true, actual);
    }

    @Test
    void addPlayers() {
    }

    @Test
    void removePlayer() {
    }

    @Test
    void checkIfEnoughPlayersNotEnough() throws Exception {
        lobby.addPlayer(testUser);
        boolean actual = lobby.checkIfEnoughPlayers();
        assertEquals(false, actual);
    }

    /*
    @Test
    void checkIfEnoughPlayersEnough() throws Exception {
        for (int i = 0; i < 5; i++){
            lobby.addPlayer(testUser);
        }
        boolean actual = lobby.checkIfEnoughPlayers();
        assertEquals(true, actual);
    }
    */

    @Test
    void setGamePlayers() throws Exception {
        ArrayList<User> expected = players;
        lobby.addPlayer(testUser);
        lobby.addPlayer(testUser2);
        lobby.setGamePlayers();
        Match match = gameManager.getMatch(0L);
        //expected.clear();
        assertEquals(expected, match.getMatchPlayers());

    }
}