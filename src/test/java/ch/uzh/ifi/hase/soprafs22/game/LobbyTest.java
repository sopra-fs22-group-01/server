package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {
    private User testUser = new User();
    private User testUser2 = new User();
    private Lobby lobby;
    private ArrayList<User> players;

    @BeforeEach
    void setUp() {
        this.testUser = new User();
        this.testUser.setIsReady(ReadyStatus.READY);
        this.testUser2 = new User();
        this.testUser2.setIsReady(ReadyStatus.UNREADY);
        this.players = new ArrayList<>();
        this.players.add(testUser);
        this.players.add(testUser2);
        this.lobby = new Lobby();

    }

    @AfterEach
    void tearDown() {
        this.testUser = null;
        this.testUser2 = null;
        this.lobby = null;
        players.clear();
    }

    @Test
    void checkIfAllReadyFalse() {
        boolean actual = lobby.checkIfAllReady(players);
        assertEquals(false, actual);
    }

    @Test
    void checkIfAllReadyTrue() {
        testUser2.setIsReady(ReadyStatus.READY);
        boolean actual = lobby.checkIfAllReady(players);
        assertEquals(true, actual);
    }

    @Test
    void addPlayers() {
    }

    @Test
    void removePlayer() {
    }

    @Test
    void checkIfEnoughPlayersNotEnough() {
        lobby.addPlayers(testUser);
        boolean actual = lobby.checkIfEnoughPlayers();
        assertEquals(false, actual);
    }

    @Test
    void checkIfEnoughPlayersEnough() {
        for (int i = 0; i < 5; i++){
            lobby.addPlayers(testUser);
        }
        boolean actual = lobby.checkIfEnoughPlayers();
        assertEquals(true, actual);
    }

    /*
    @Test
    void setGamePlayers() {
        Match match = Match.getInstance();
        ArrayList<User> expected = players;
        lobby.addPlayers(testUser);
        lobby.addPlayers(testUser2);
        lobby.setGamePlayers();
        assertEquals(expected, match.getGamePlayers());

    }
    */
}