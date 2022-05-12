package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.VotingStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    private Match testMatch;
    private ArrayList<User> testUsers;

    @BeforeEach
    void setUp() {
        testMatch = new Match(0L);
        testUsers = new ArrayList<>();
        testMatch.createRound();
    }

    @AfterEach
    void tearDown() {
        testMatch = null;
    }

    @Test
    void testCreateRound() {
        Round testRound = testMatch.getRound();
        assertNotNull(testRound);
    }

    @Test
    void testCreateScoreBoard() {
        testMatch.createScoreBoard();
        ScoreBoard testScoreBoard = testMatch.getScoreBoard();
        assertNotNull(testScoreBoard);
    }

    @Test
    void testGetMatchPlayers() {
        User testUser = new User();
        testUsers.add(testUser);
        testMatch.setMatchPlayers(testUsers);
        ArrayList<User> actual = testMatch.getMatchPlayers();
        assertEquals(testUsers.size(), actual.size());
    }

    @Test
    void testGetId() {
        assertEquals(0L, testMatch.getId());
    }

    @Test
    void testUpdatePlayerScores() {
        User testUser = new User();
        testUsers.add(testUser);
        WhiteCard testWhiteCard = new WhiteCard(testUser);
        testMatch.setMatchPlayers(testUsers);
        testMatch.getRound().setChosenCard(testWhiteCard);
        testMatch.updatePlayerScores();
        int actual = testUser.getScore();
        assertEquals(1, actual);
    }

    @Test
    public void initialApiRequestStatus_is_INCOMPLETE() {
        assertEquals(VotingStatus.INCOMPLETE,testMatch.getVotingStatus());
    }

    @Test
    public void incrementRequestStatusCountAndCheckStatus_changesStatus() {
        User testUser = new User();
        testUsers.add(testUser);
        testMatch.setMatchPlayers(testUsers);
        testMatch.incrementVoteCountAndCheckStatus();
        //expect the status to change since ApiStatusCount got incremented and therefore equals the amount of players
        assertEquals(VotingStatus.COMPLETE,testMatch.getVotingStatus());
    }

    @Test
    public void incrementRequestStatusCountAndCheckStatus_doesnt_changeStatus() {
        User testUser = new User();
        User testUser2 = new User();
        testUsers.add(testUser);
        testUsers.add(testUser2);
        testMatch.setMatchPlayers(testUsers);
        testMatch.incrementVoteCountAndCheckStatus();
        //expect the status to change since ApiStatusCount got incremented and therefore equals the amount of players
        assertEquals(VotingStatus.INCOMPLETE,testMatch.getVotingStatus());
    }

}