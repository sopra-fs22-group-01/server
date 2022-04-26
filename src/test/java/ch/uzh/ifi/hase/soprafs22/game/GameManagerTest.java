package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    private GameManager gameManager;
    private User testUser = new User();
    private User testUser2 = new User();
    private ArrayList<User> players;

    @BeforeEach
    void setUp() {
        this.gameManager = GameManager.getInstance();
        this.testUser = new User();
        this.testUser2 = new User();
        this.players = new ArrayList<>();
        this.players.add(testUser);
        this.players.add(testUser2);
    }

    @AfterEach
    void tearDown() {
        this.testUser = null;
        this.testUser2 = null;
        this.gameManager.resetGameManager();
        players.clear();
    }

/*    @Test
    void createMatch() throws Exception {
        gameManager.createMatch(players);
        Match actualMatch = gameManager.getMatch(0L);

        Match expectedMatch = new Match(0L);
        expectedMatch.setMatchPlayers(players);

        assertEquals(expectedMatch.getId(), actualMatch.getId());
        assertEquals(expectedMatch.getMatchPlayers(), actualMatch.getMatchPlayers());
    }*/

    @Test
    void createLobby() throws Exception {
        gameManager.createLobby();
        Lobby actualLobby = gameManager.getLobby(0L);

        Lobby expectedLobby = new Lobby(0L);

        assertEquals(expectedLobby.getId(), actualLobby.getId());
    }

    @Test
    void getLobby() {
    }

    @Test
    void getMatch() {
    }
}