package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.controller.UserController;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.helpers.VotingStatus;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    private GameManager gameManager = new GameManager();
    private User testUser1 = new User();
    private User testUser2 = new User();
    private ArrayList<User> players;

    @BeforeEach
    void setUp() {
        gameManager.reset();
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
        this.gameManager.reset();
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
        assertEquals(emptyList, gameManager.getAllLobbies());
    }

    @Test
    void isGameOver_true() {
        Match testMatch = gameManager.createMatch(players,0L);
        Round currentRound = testMatch.getRound();
        for(User player:players){
            player.setScore(currentRound.getMaxScore());
        }
        currentRound.startNewRound();

        assertTrue(gameManager.isGameOver(testMatch));

    }

    @Test
    void isGameOver_false() {
        Match testMatch = gameManager.createMatch(players,0L);
        Round currentRound = testMatch.getRound();
        for(User player:players){
            player.setScore(1);
        }
        currentRound.startNewRound();

        assertFalse(gameManager.isGameOver(testMatch));

    }



    @Test
    void evaluateNewRoundStart_true() throws IncorrectIdException {
        Match testMatch = gameManager.createMatch(players,0L);
        Round currentRound = testMatch.getRound();
        for(User player:players){
            player.setScore(1);
        }
        currentRound.startNewRound();

        assertTrue(gameManager.evaluateNewRoundStart(0L));
    }

    @Test
    void evaluateNewRoundStart_false() throws IncorrectIdException {
        Match testMatch = gameManager.createMatch(players,0L);
        Round currentRound = testMatch.getRound();
        for(User player:players){
            player.setScore(currentRound.getMaxScore());
        }
        currentRound.startNewRound();

        assertFalse(gameManager.evaluateNewRoundStart(0L));
    }


    @Test
    void deleteLobby_success() throws Exception {
        GameService gameService = new GameService(gameManager);
        Lobby testLobby = gameManager.createLobby();

        int sizeOfLobbiesBeforeDeletion = gameManager.getAllLobbies().size();

        assertEquals(1,sizeOfLobbiesBeforeDeletion);


        gameService.deleteLobby(testLobby.getId());

        int sizeOfLobbiesAfterDeletion = gameManager.getAllLobbies().size();

        assertEquals(0, sizeOfLobbiesAfterDeletion);
    }

    //TODO: Check this test when all tests are run together. seems to fail sometimes
    @Test
    void startNewRound_success() throws IncorrectIdException {
        Match testMatch = gameManager.createMatch(players,0L);
        testMatch.incrementVoteCountAndCheckStatus();

        gameManager.startNewRound(testMatch.getId());
        assertEquals(VotingStatus.INCOMPLETE,testMatch.getVotingStatus());
        assertFalse(testMatch.isScoresUpdated());
    }
}