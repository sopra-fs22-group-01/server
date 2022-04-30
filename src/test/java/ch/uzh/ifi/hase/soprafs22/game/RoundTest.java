package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Countdown;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    private Round testRound;
    private WhiteCard testWhiteCard;
    private ArrayList<WhiteCard> testChosenCards;

    @BeforeEach
    void setUp() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);
        testRound = new Round(users);
        testWhiteCard = new WhiteCard();
        testWhiteCard.createCard();
        testChosenCards = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        testRound = null;
        testChosenCards = null;
    }

    @Test
    void testGetCountdown(){
        Countdown actual = testRound.getCountdown();
        assertNotNull(actual);
    }

    @Test
    void testStartNewRound() {
        boolean actual = testRound.startNewRound();
        assertTrue(actual);
    }

    @Test
    void testSetBlackCard() {
        BlackCard blackCard = new BlackCard();
        blackCard.createCard();
        testRound.setBlackCard(blackCard);
        BlackCard actual = testRound.getBlackCard();
        assertEquals(blackCard, actual);
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
}

