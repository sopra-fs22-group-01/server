package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {
    private Hand testHand;
    private User testUser;

    @BeforeEach
    void setUp(){
        testUser = new User();
        testHand = new Hand(testUser);
    }

    @AfterEach
    void tearDown(){
        testUser = null;
        testHand = null;
    }

    @Test
    void testCreateHand() {
        testHand.createHand();
        ArrayList<WhiteCard> actual = testHand.getUserHand();
        assertEquals(10, actual.size());
    }

    @Test
    void testUpdateHand(){
        testHand.createHand();
        WhiteCard testCard = testHand.getUserHand().get(0);
        testHand.setChosenCard(testCard);
        testHand.updateHand();
        ArrayList<WhiteCard> actual = testHand.getUserHand();
        assertEquals(10, actual.size());
    }

    @Test
    void testResetChosenCard(){
        testHand.createHand();
        WhiteCard testCard = testHand.getUserHand().get(0);
        testHand.setChosenCard(testCard);
        testHand.resetChosenCard();
        assertNull(testHand.getChosenCard());
    }

    @Test
    void testGetCardsFromHand(){
        testHand.createHand();
        ArrayList<WhiteCard> whiteCards = testHand.getCardsFromHand();
        assertEquals(10, whiteCards.size());
    }
}