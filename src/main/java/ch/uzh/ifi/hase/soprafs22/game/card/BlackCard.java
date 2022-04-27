package ch.uzh.ifi.hase.soprafs22.game.card;

import ch.uzh.ifi.hase.soprafs22.JSON.ReadJSONFile;

public class BlackCard implements Card {
    private String text = "";

    public BlackCard(){}

    public String getText(){
        String s = text.replace("_", "____");
        return s;
    }

    public void createCard() {
        // read JSON file and get a card text
        ReadJSONFile readJSONFile = ReadJSONFile.getInstance();
        this.text = readJSONFile.getBlackCardText();
    }
}
