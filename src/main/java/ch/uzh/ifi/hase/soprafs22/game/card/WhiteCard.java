package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.constant.CardType;

public class WhiteCard implements Card {
    private CardType cardType = CardType.EMPTY;
    private String text = "";

    public CardType getCardType(){
        return cardType;
    }

    public String getText(){
        return text;
    }

    public Card createCard(){
        Card newCard = new WhiteCard();
        return newCard;
    }

}