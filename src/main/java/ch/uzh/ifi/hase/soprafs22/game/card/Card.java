package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.constant.CardType;

public interface Card {
    //mistake in UML: interface can only contain constant variables but each card has to have different texts etc.
    CardType getCardType();
    String getText();
    Card createCard();
}
