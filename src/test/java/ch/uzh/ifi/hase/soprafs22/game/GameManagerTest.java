package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    private GameManager gameManager;
    private User testUser1 = new User();
    private User testUser2 = new User();
    private ArrayList<User> players;

    @BeforeEach
    void setUp() {
        this.gameManager = GameManager.getInstance();
        this.testUser1 = new User();
        this.testUser2 = new User();
        this.players = new ArrayList<>();
        this.players.add(testUser1);
        this.players.add(testUser2);
    }

    @AfterEach
    void tearDown() {
        this.testUser1 = null;
        this.testUser2 = null;
        this.gameManager.resetGameManager();
        players.clear();
    }

    @Test
    void createMatch() throws Exception {
        gameManager.createMatch(players, 0L);
        Match actualMatch = gameManager.getMatch(0L);

        Match expectedMatch = new Match(0L);
        expectedMatch.setMatchPlayers(players);

        assertEquals(expectedMatch.getId(), actualMatch.getId());
        assertEquals(expectedMatch.getMatchPlayers(), actualMatch.getMatchPlayers());
    }

    @Test
    void createLobby() throws Exception {
        Lobby actualLobby = gameManager.createLobby();
        Lobby expectedLobby = gameManager.getLobby(0L);
        assertEquals(expectedLobby.getId(), actualLobby.getId());
    }

    @Test
    void getLobbyLobbyNotFound() {
        assertThrows(IncorrectIdException.class, () -> gameManager.getLobby(20L));
    }

    @Test
    void getMatchMatchNotFound() {
        assertThrows(IncorrectIdException.class, () -> gameManager.getMatch(20L));
    }

    @Test
    void getAllLobby() {
        ArrayList<Lobby> emptyList = new ArrayList<>();
        assertEquals(emptyList, gameManager.getAllLobby());
    }

}