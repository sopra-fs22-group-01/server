package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.JSON.ReadJSONFile;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class WhiteCard implements Card {
    // maybe id should be static, which is a class variable to keep track of the cards?
    private Long id;
    private User owner;
    private int score;
    private String text = "";

    public WhiteCard(){}

    public WhiteCard(User owner){
        this.owner = owner;
    }

    public String getText(){
        return text;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int s){
        this.score = s;
    }

    public User getOwner(){
        return owner;
    }

    public void createCard(){
        // setting score to 0
        setScore(0);

        // setting text by getting the text from the ReadJSONFile
        ReadJSONFile readJSONFile = ReadJSONFile.getInstance();
        this.text = readJSONFile.getWhiteCardText();
    }
}