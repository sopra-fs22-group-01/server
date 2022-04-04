package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.constant.CardType;

public class BlackCard implements Card {
    private CardType cardType = CardType.EMPTY;
    private String text = "";

    private BlackCard(String text){
        this.text = text;
    }

    public CardType getCardType(){
        return cardType;
    }

    public String getText(){
        return text;
    }

    public Card createCard(String text){
        return new BlackCard(text);
    }
}
