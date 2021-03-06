package ch.uzh.ifi.hase.soprafs22.game.card;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhiteCardTest {
    WhiteCard testWhiteCard;

    @BeforeEach
    void setUp() {
        this.testWhiteCard = new WhiteCard();
    }

    @AfterEach
    void tearDown() {
        this.testWhiteCard = null;
    }

    @Test
    void testGetNoText() {
        String actual = testWhiteCard.getText();
        assertEquals("", actual);
    }

    @Test
    void testCreateCard() {
        testWhiteCard.createCard();
        String actual = testWhiteCard.getText();
        assertNotEquals("", actual);
    }
}