package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.constant.CardType;

public class BlackCard implements Card {
    private CardType cardType = CardType.EMPTY;
    private String text = "";

    public BlackCard(){
    }

    public CardType getCardType(){
        return cardType;
    }

    public String getText(){
        return text;
    }

    public void createCard() {
    }
}
