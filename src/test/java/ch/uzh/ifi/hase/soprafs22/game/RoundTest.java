package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
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
        ArrayList<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);
        testRound = new Round(users);
        testWhiteCards = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        testRound = null;
        testWhiteCards = null;
    }

    @Test
    void testStartNewRound() {
        boolean actual = testRound.startNewRound();
        assertTrue(actual);
    }

    @Test
    void testSetRoundPlayers() {
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
    void testUpdateHands() {
    }

    @Test
    void testSetChosenCard() {
    }

    @Test
    void testGetAllChosenCardsSizeNull() {
        testRound.startNewRound();
        ArrayList<WhiteCard> chosenCards = testRound.getAllChosenCards();
        assertEquals(0,chosenCards.size());
    }

    @Test
    void testGetRoundWinnerCardsNull() {
        ArrayList<WhiteCard> actual = testRound.getRoundWinnerCards();
        assertEquals(0, actual.size());
    }
}

