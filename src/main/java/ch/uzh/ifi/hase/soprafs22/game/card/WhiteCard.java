package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.constant.CardType;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class WhiteCard implements Card {
    private Long id;
    private User owner;
    private int score;
    private CardType cardType = CardType.EMPTY;
    private String text = "";

    public WhiteCard(){}

    public WhiteCard(User owner){
        this.owner = owner;
    }

    public CardType getCardType(){
        return cardType;
    }

    public String getText(){
        return text;
    }

    public void createCard(){
        //setting score to 0
        score = 0;
        //setting text by getting the text from the ReadJSONFile
        //text = ReadJSONFile.getWhiteCardText()
    }
}