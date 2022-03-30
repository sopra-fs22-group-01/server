package ch.uzh.ifi.hase.soprafs22.game.deck;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.card.Card;

import java.util.ArrayList;

public class WhiteDeck implements Deck{
    private ArrayList<Card> content = new ArrayList<Card>();

    public Deck createDeck() {
        WhiteDeck newDeck = new WhiteDeck();
        return newDeck;
    }

    public Card draw() {return null; }

    public void shuffle(){}
}
