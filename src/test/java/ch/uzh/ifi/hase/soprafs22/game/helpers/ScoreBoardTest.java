package ch.uzh.ifi.hase.soprafs22.game.helpers;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {
    private User testUser;
    private User testUser2;
    private User testUser3;
    private ScoreBoard scoreBoard = new ScoreBoard();
    private ArrayList<User> players;

    @BeforeEach
    void setUp() {
        this.testUser = new User();
        this.testUser.setScore(0);
        this.testUser.setUsername("testUsername");

        this.testUser2 = new User();
        this.testUser2.setScore(0);
        this.testUser2.setUsername("testUsername2");

        this.testUser3 = new User();
        this.testUser3.setScore(4);
        this.testUser3.setUsername("testUsername3");

        this.players = new ArrayList<>();
        this.players.add(testUser);
        this.players.add(testUser2);
        this.players.add(testUser3);

    }

    @AfterEach
    void tearDown() {
        this.testUser = null;
        this.testUser2 = null;
        this.testUser3 = null;
        players.clear();
    }

    /*
    @Test
    void testUpdateScore() {
        //tests if the user's score gets correct updated when they achieved a new point
        int expected = 1;
        scoreBoard.updateScore(testUser, 1);
        int actual = testUser.getScore();
        assertEquals(expected, actual);
    }
     */

    @Test
    void testGetPlayersOfHighestRank() {
        //tests if the players of the highest rank are returned
        ArrayList<User> actual = scoreBoard.getPlayersOfHighestRank(players);
        assertEquals(testUser3, actual.get(0));
    }

    @Test
    void testGetPlayersOfHighestRankOneWinner() {
        //tests if the players of the highest rank are returned
        int expected = 1;
        ArrayList<User> winners = scoreBoard.getPlayersOfHighestRank(players);
        int actual = winners.size();
        assertEquals(expected, actual);

    }

    /*
    @Test
    void testGetRanking() {
        HashMap ranking = scoreBoard.getRanking(players);
        assertEquals(2, ranking.get(testUser.getUsername()));
        assertEquals(2, ranking.get(testUser2.getUsername()));
        assertEquals(1, ranking.get(testUser3.getUsername()));

    }
    */
}