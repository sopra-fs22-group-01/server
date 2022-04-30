package ch.uzh.ifi.hase.soprafs22.game.helpers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RankingTest {
    private Ranking ranking;
    private String testUsername;

    @BeforeEach
    void setUp() {
        testUsername = "";
        this.ranking = new Ranking(1, testUsername, 0);
    }

    @AfterEach
    void tearDown() {
        testUsername = null;
        this.ranking = null;
    }

    @Test
    void getRank() {
        assertEquals(1, ranking.getRank());
    }

    @Test
    void getUsername() {
        assertEquals(testUsername, ranking.getUsername());
    }

    @Test
    void setRank() {
        ranking.setRank(1);
        assertEquals(1, ranking.getRank());
    }

    @Test
    void setUsername() {
        ranking.setUsername("testUsername2");
        assertEquals("testUsername2", ranking.getUsername());
    }

    @Test
    void setScore() {
        ranking.setScore(1);
        assertEquals(1, ranking.getScore());
    }

    @Test
    void getScore() {
        assertEquals(0, ranking.getScore());
    }
}