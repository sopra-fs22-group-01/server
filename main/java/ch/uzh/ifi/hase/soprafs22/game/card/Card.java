package ch.uzh.ifi.hase.soprafs22.game.card;


public interface Card {
    //mistake in UML: interface can only contain constant variables but each card has to have different texts etc.
    String getText();
    void createCard(); //setting for each card a unique text
}
