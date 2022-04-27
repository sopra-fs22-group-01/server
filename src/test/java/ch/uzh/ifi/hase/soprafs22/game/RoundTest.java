package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    private Round testRound;
    private ArrayList<WhiteCard> testWhiteCards;

    @BeforeEach
    void setUp() {
        /*
        testRound.startNewRound();
        testWhiteCards = new ArrayList<>();*/
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testStartNewRound() {
    }

    @Test
    void testSetRoundPlayers() {
    }

    @Test
    void testSetBlackCard() {
    }

    @Test
    void testGetBlackCard() {
    }

    @Test
    void testUpdateHands() {
    }

    @Test
    void testSetChosenCard() {
    }

    @Test
    void testGetAllChosenCards() {
    }

    @Test
    void testGetRoundWinnerNull() {
        // have not written yet since the class is not ready to be tested
        /*User actual = testRound.getRoundWinner();
        assertNull(actual);*/
    }
}