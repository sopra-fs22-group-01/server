package ch.uzh.ifi.hase.soprafs22.game.deck;

import ch.uzh.ifi.hase.soprafs22.game.card.Card;

import java.util.ArrayList;

public class BlackDeck implements Deck{
    private ArrayList<Card> content = new ArrayList<Card>();

    public Deck createDeck() {
        BlackDeck newDeck = new BlackDeck();
        return newDeck;
    }

    public Card draw() {
        return null;
    }

    public void shuffle() {}
    //here comes black deck implementation
}
