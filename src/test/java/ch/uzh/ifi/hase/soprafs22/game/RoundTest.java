package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Countdown;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    private Round testRound;
    private WhiteCard testWhiteCard;
    private ArrayList<WhiteCard> testChosenCards;

    @BeforeEach
    void setUp() {
        ArrayList<User> testUsers = new ArrayList<>();
        User testUser = new User();
        testUser.setId(0L);
        testUsers.add(testUser);
        testRound = new Round(testUsers);
        testWhiteCard = new WhiteCard();
        testWhiteCard.createCard();
        testWhiteCard.setOwner(testUser);
        testChosenCards = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        testRound = null;
        testChosenCards = null;
    }


  /*  @Test
    void testStartNewRound() {
        actual = testRound.startNewRound();
        assertTrue(actual);
    }*/

    @Test
    void testSetAndGetBlackCard() {
        BlackCard blackCard = new BlackCard();
        blackCard.createCard();
        testRound.setBlackCard(blackCard);
        BlackCard actual = testRound.getBlackCard();
        assertEquals(blackCard, actual);
    }

    @Test
    void testGetHandByUserId(){
        testRound.startNewRound();
        testRound.setChosenCard(testWhiteCard);
        Hand actual = testRound.getHandByUserId(testWhiteCard.getOwner().getId());
        assertNotNull(actual);
    }

    @Test
    void testSetChosenCard() {
        testRound.startNewRound();
        testRound.setChosenCard(testWhiteCard);
        ArrayList<WhiteCard> testChosenCards = testRound.getAllChosenCards();
        assertEquals(1, testChosenCards.size());
    }

    @Test
    void testGetHands(){
        testRound.startNewRound();
        ArrayList<Hand> actual = testRound.getHands();
        assertEquals(1, actual.size());
    }

    @Test
    void testGetAllChosenCardsSizeNull() {
        testRound.startNewRound();
        testChosenCards = testRound.getAllChosenCards();
        assertEquals(0,testChosenCards.size());
    }

    @Test
    void testGetRoundWinnerCardsNull() {
        testChosenCards = testRound.getRoundWinnerCards();
        assertEquals(0, testChosenCards.size());
    }

    @Test
    void testGetRoundWinnerCardsNotNull(){
        testRound.startNewRound();
        testRound.setChosenCard(testWhiteCard);
        testChosenCards = testRound.getRoundWinnerCards();
        assertEquals(1, testChosenCards.size());
    }

    @Test
    void testIncrementCardScores(){
        testRound.startNewRound();
        testRound.setChosenCard(testWhiteCard);
        testRound.incrementCardScores(0L);
        int actual = testWhiteCard.getScore();
        assertEquals(1, actual);
    }


    @Test
    public void startCountdownSuccess() throws InterruptedException {
        testRound.startSelectionCountdown();
        testRound.startVotingCountdown();
        testRound.startRankingCountdown();

        int startTimeSelection = testRound.getSelectionTime();
        int startTimeVoting = testRound.getVotingTime();
        int startTimeRanking = testRound.getSelectionTime();


        Thread.sleep(1050);


        int selectionTimeAfterOneSec = testRound.getSelectionTime();
        int votingTimeAfterOneSec = testRound.getVotingTime();
        int rankingTimeAfterOneSecond = testRound.getSelectionTime();


        assertTrue(startTimeSelection > selectionTimeAfterOneSec);
        assertTrue(startTimeVoting > votingTimeAfterOneSec);
        assertTrue(startTimeRanking > rankingTimeAfterOneSecond);

//        assertEquals(selectionTimeAfterOneSec,startTimeSelection);
//        assertTrue(startTimeVoting > votingTimeAfterOneSec);
//        assertEquals(startTimeRanking > rankingTimeAfterOneSecond);

    }
}

