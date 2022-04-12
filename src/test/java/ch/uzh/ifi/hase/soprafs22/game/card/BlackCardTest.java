package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.constant.CardType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackCardTest {
    BlackCard testBlackCard;

    @BeforeEach
    void setUp() {
        this.testBlackCard = new BlackCard();
    }

    @AfterEach
    void tearDown() {
        this.testBlackCard = null;
    }

    @Test
    void testGetCardType() {
        CardType actual = testBlackCard.getCardType();
        CardType expected = CardType.EMPTY;
        assertEquals(actual, expected);
    }

    @Test
    void testGetNoText() {
        String actual = testBlackCard.getText();
        String expected = "";
        assertEquals(actual, expected);
    }

    @Test
    void testCreateCard() {
        testBlackCard.createCard();
        String actual = testBlackCard.getText();
        assertNotEquals("", actual);
    }
}