package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {
    private User testUser1;
    private Lobby lobby;
    private GameManager gameManager = new GameManager();

    @BeforeEach
    void setUp() {
        this.gameManager.reset();
        this.testUser1 = new User();
        this.testUser1.setId(1L);
        this.testUser1.setIsReady(ReadyStatus.UNREADY);
        this.lobby = new Lobby(1L);




    }

    @Test
    void getId(){
        assertEquals(1L, lobby.getId());
    }
    @Test
    void setId(){
        lobby.setId(3L);
        assertEquals(3L, lobby.getId());
    }
    @Test
    void getCurrentPlayerCount() throws Exception {
        lobby.addPlayer(testUser1);
        assertEquals(1, lobby.getCurrentPlayerCount());
    }
    @Test
    void getAndSetLobbyStatus() {
        lobby.setLobbyStatus(LobbyStatus.Waiting);
        assertEquals(LobbyStatus.Waiting, lobby.getLobbyStatus());
    }

    @Test
    void checkIfAllReadyFalse() throws Exception {
        lobby.addPlayer(testUser1);
        testUser1.setIsReady(ReadyStatus.UNREADY);
        boolean actual = lobby.checkIfAllReady();
        assertFalse(actual);
    }

    @Test
    void checkIfAllReadyTrue() throws Exception {
        lobby.addPlayer(testUser1);
        testUser1.setIsReady(ReadyStatus.READY);
        boolean actual = lobby.checkIfAllReady();
        assertTrue(actual);
    }

    @Test
    void addCurrentPlayers() throws Exception {
        lobby.addPlayer(testUser1);
        ArrayList<User> allCurrentPlayers = lobby.getCurrentPlayers();
        assertEquals(testUser1, allCurrentPlayers.get(0));
    }

    @Test
    void addCurrentPlayersUserAlreadyInLobby() throws Exception {
        lobby.addPlayer(testUser1);
        assertThrows(Exception.class, () -> lobby.addPlayer(testUser1));
    }


    @Test
    void removePlayer() throws Exception {
        lobby.addPlayer(testUser1);
        lobby.removePlayer(testUser1.getId());
        ArrayList<User> allCurrentPlayers = lobby.getCurrentPlayers();
        assertEquals(0, allCurrentPlayers.size());
    }

    @Test
    void removePlayerUserNotInLobby() {
        assertThrows(Exception.class, () -> lobby.removePlayer(testUser1.getId()));
    }


    @Test
    void checkIfEnoughPlayersNotEnough() {
        boolean actual = lobby.checkIfEnoughPlayers();
        assertFalse(actual);
    }

    @Test
    void checkIfEnoughPlayersEnough() throws Exception {
        lobby.addPlayer(testUser1);
        boolean actual = lobby.checkIfEnoughPlayers();
        assertTrue(actual);
    }


    @Test
    void setReadyStatusPlayerUnready() throws Exception {
        lobby.addPlayer(testUser1);
        lobby.setReadyStatus(testUser1.getId());
        assertTrue(lobby.checkIfAllReady());
    }

    @Test
    void setReadyStatusPlayerReady() throws Exception {
        lobby.addPlayer(testUser1);
        lobby.setReadyStatus(testUser1.getId()); //testUser1 ready
        lobby.setReadyStatus(testUser1.getId()); //testUser1 unready
        assertFalse(lobby.checkIfAllReady());
    }


}