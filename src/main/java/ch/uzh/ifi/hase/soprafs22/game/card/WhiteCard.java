package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.JSON.ReadJSONFile;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class WhiteCard implements Card { //TEST
    // maybe id should be static, which is a class variable to keep track of the   cards
    private Long id;
    private User owner;
    private int score;
    private String text = "";

    //constructor
    public WhiteCard(User owner){
        this.owner = owner;
    }

    public Long getId(){return this.id;}
    public void setId(Long id){this.id = id;}


    public User getOwner(){return this.owner;}
    public void setOwner(User owner){this.owner = owner;}

    public int getScore(){return this.score;}
    public void setScore(int score){this.score = score;}

    public String getText(){return this.text;}
    public void setText(String text){this.text = text;}


    public WhiteCard(){}

    public void createCard(){
        // setting score to 0
        score = 0;

        // setting text by getting the  text from the ReadJSONFile
        ReadJSONFile readJSONFile = ReadJSONFile.getInstance();
        this.text = readJSONFile.getWhiteCardText();
    }
    public void incrementCard(){
        this.score = score + 1;
    }
}