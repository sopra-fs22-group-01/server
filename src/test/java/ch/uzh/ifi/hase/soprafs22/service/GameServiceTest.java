package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
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
    }
    @AfterEach
    void tearDown() {
        this.testUser = null;
        this.testUser2 = null;
        this.gameManager.resetGameManager();
        players.clear();
    }

    @Test
    void getRulesFromText_success() throws Exception {

        ArrayList<String> testArray = gameService.getRulesFromText();
        String expected = "There  are two types of cards: Black cards and white cards.";
        assertEquals(expected,testArray.get(0) );

    }


  /*  @Test
    void incrementCardScore_success() throws Exception {

        gameManager.createMatch(players);

        Match actualMatch = gameManager.getMatch(0L);
        actualMatch.createHands();
        Round testRound = actualMatch.getRound();
        testRound.getAllChosenCards();

    }*/

}
