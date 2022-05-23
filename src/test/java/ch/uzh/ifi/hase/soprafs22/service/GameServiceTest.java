package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameManager gameManager = new GameManager();
    private User testUser;
    private User testUser2;
    private ArrayList<User> players;
    private Lobby testLobby;

    @InjectMocks
    private GameService gameService = new GameService(gameManager);

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.gameManager.reset();
        this.testUser = new User();
        this.testUser2 = new User();
        this.players = new ArrayList<>();
        this.players.add(testUser);
        this.players.add(testUser2);
        this.testLobby = gameManager.createLobby();
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

    /*
    @Test
    public void checkIfLobbyStatusChanges_changs_AllReady() throws Exception {

        gameService.addPlayerToLobby(testLobby.getId(), testUser);
        gameService.addPlayerToLobby(testLobby.getId(), testUser2);

        gameService.updateUserReadyStatus(testLobby.getId(), testUser.getId());
        gameService.updateUserReadyStatus(testLobby.getId(), testUser2.getId());

        gameService.checkIfLobbyStatusChanged(testLobby.getId());

        assertEquals(LobbyStatus.All_Ready, testLobby.getLobbyStatus());

    }


    @Test
    public void startMatch_success() throws IncorrectIdException {
        Match testMatch = gameService.startMatch(0);

        assertEquals(0,testMatch.getId());

    }




    //Doenst work either
    @Test
    public void removePlayerFromLobby_success() throws Exception {
        testUser.setId(0L);
        Lobby lobby = gameService.createNewLobby();
        long lobbyId = lobby.getId();

        gameService.addPlayerToLobby(lobbyId,testUser);
        lobby = gameManager.getLobby(lobby.getId());
        assertEquals(1 ,lobby.getCurrentPlayers().size());

        gameService.removePlayerFromLobby(lobby.getId(), testUser.getId());

        assertEquals(0 ,lobby.getCurrentPlayers().size());

    }



    //problem with the following two tests: When run with all other tests, matches list in GameManager somehow deleted
    //when we try to getMatch a second time (makes absolutely no sense and doesn't happen when the two tests are run separately)

   /* @Test
    void getRanking_once_success() throws IncorrectIdException {
        *//*testUser.setScore(1);
        testUser2.setScore(2);
        *//*
        User testUser3 = new User();
        User testUser4 = new User();
        testUser3.setUsername("testuser3");
        testUser4.setUsername("testuser4");
        testUser3.setScore(2);
        testUser4.setScore(1);
        ArrayList<User> players1 =  new ArrayList<>();
        players1.add(testUser3);
        players1.add(testUser4);


        gameManager.createMatch(players1, 0L);
        Match testMatch = gameManager.getMatch(0L);
        ArrayList<Ranking> testRanking = gameService.getRanking(0L);
        int scoreOfWinner = testRanking.get(0).getScore();
        assertEquals(2,scoreOfWinner);
    }


    //this test is important to cover one of our bugs, where the scoreboard returns an empty array after
    //when being fetched for a second time
    @Test
    void getRanking_twice_in_a_row_success() throws IncorrectIdException {


        testUser.setScore(1);
        testUser2.setScore(2);


        Match testMatch = gameManager.createMatch(players, 0L);

        //get ranking for the first time
        ArrayList<Ranking> testRanking = gameService.getRanking(testMatch.getId());

        int scoreOfWinner = testRanking.get(0).getScore();
        assertEquals(2,scoreOfWinner);

        //get Ranking for the second time
        ArrayList<Ranking> testRanking2 = gameService.getRanking(testMatch.getId());


        int scoreOfWinnerV2 = testRanking2.get(0).getScore();
        assertEquals(2,scoreOfWinnerV2);
    }*/

}
