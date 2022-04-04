package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.constant.CardType;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class WhiteCard implements Card {
    private Long id;
    private User owner;
    private int score;
    private CardType cardType = CardType.EMPTY;
    private String text = "";

    private WhiteCard(String text){
        this.text = text;
    }

    public CardType getCardType(){
        return cardType;
    }

    public String getText(){
        return text;
    }

    public Card createCard(String text){
        return new WhiteCard(text);
    }
}