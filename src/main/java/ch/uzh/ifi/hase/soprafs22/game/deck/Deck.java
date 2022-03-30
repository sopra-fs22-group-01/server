package ch.uzh.ifi.hase.soprafs22.game.deck;

import ch.uzh.ifi.hase.soprafs22.game.card.Card;

public interface Deck {
    Deck createDeck();
    Card draw();
    void shuffle();
}

