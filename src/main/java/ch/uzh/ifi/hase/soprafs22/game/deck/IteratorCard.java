package ch.uzh.ifi.hase.soprafs22.game.deck;

import ch.uzh.ifi.hase.soprafs22.game.card.Card;

public interface IteratorCard {
    boolean hasNext();
    Card next();
}
