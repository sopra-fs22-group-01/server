package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.LaughStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.VotingStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

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

    @Test
    public void decreaseSuperVote_success() {
        int numberOfSuperVotes;

        User testUser1 = new User();
        User testUser2 = new User();
        testUser1.setId(0L);
        testUser2.setId(1L);

        testUsers.add(testUser1);
        testUsers.add(testUser2);

        testMatch.setMatchPlayers(testUsers);

        //number of superVotes before decreaseSuperVote should be 1
        numberOfSuperVotes = testUser1.getSuperVote();
        assertEquals(1,numberOfSuperVotes);

        //number of superVotes after decreaseSuperVote() should be 0
        testMatch.decreaseSuperVote(testUser1.getId());
        numberOfSuperVotes = testUser1.getSuperVote();

        assertEquals(0,numberOfSuperVotes);
    }

    @Test
    public void decreaseSuperVote_error() {

        User testUser1 = new User();
        User testUser2 = new User();
        testUser1.setId(0L);
        testUser2.setId(1L);
        testUsers.add(testUser1);
        testUsers.add(testUser2);
        testMatch.setMatchPlayers(testUsers);



        //number of superVotes after decreaseSuperVote() should be 0
        testMatch.decreaseSuperVote(testUser1.getId());
        //when number of supervotes >= 0 and decreaseSuperVote() is called, BadRequest should be thrown
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            testMatch.decreaseSuperVote(testUser1.getId());
        }, "No super-votes left");

        Assertions.assertEquals("400 BAD_REQUEST \"No super-votes left !\"",thrown.getMessage());
    }

    @Test
    public void updateLaughStatus_success(){
        User testUser1 = new User();
        testUser1.setId(0L);
        testUsers.add(testUser1);
        testMatch.setMatchPlayers(testUsers);
        testMatch.setLaughStatus(LaughStatus.Laughing);

        //status before updateLaughStatus() gets called
        LaughStatus statusBeforeUpdate = testMatch.getLaughStatus();
        assertEquals(LaughStatus.Laughing,statusBeforeUpdate);

        testMatch.updateLaughStatus();
        LaughStatus statusAfterUpdate= testMatch.getLaughStatus();
        assertEquals(LaughStatus.Silence,statusAfterUpdate);
    }



}