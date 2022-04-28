package ch.uzh.ifi.hase.soprafs22.JSON;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class is merely for reading JSON Against Humanity file
 *
 * How to use the class?
 * Get a ReadJSONFile instance, and it will read JSON file for you.
 * Use methods getWhiteCardText and getBlackCardText to get a String
 *
 * */

public final class ReadJSONFile {
    private static ArrayList<String> whiteCardTexts = new ArrayList<String>();
    private static ArrayList<String> blackCardTexts = new ArrayList<String>();
    private static int iteratorWhite = 0;
    private static int iteratorBlack = 0;

    // create an object of ReadJSONFile
    private volatile static ReadJSONFile uniqueInstance = new ReadJSONFile();

    private ReadJSONFile(){
        readJSON();
        eliminateDoubleBlank();
    }

    public static ReadJSONFile getInstance(){
        if(uniqueInstance == null){
            synchronized (ReadJSONFile.class){
                if(uniqueInstance == null){
                    uniqueInstance = new ReadJSONFile();
                }
            }
        }
        return uniqueInstance;
    }

    // private method to read the JSON file
    private void readJSON(){
        // create a parser for JSON
        JSONParser parser = new JSONParser();

        // try to read the file, else, catch exceptions
        try (FileReader reader = new FileReader("src/main/java/ch/uzh/ifi/hase/soprafs22/JSON/cah-cards-full.json")){
            // create an object by parsing and convert the object to JSONArray
            Object obj = parser.parse(reader);
            JSONArray a = (JSONArray) obj;

            // iterate over JSONArray
            for(Object o: a){
                JSONObject cards = (JSONObject) o;

                // for each white and black card dictionary, create another JSONArray to iterate
                JSONArray whiteCards = (JSONArray) cards.get("white");
                JSONArray blackCards = (JSONArray) cards.get("black");

                // iterate over white cards
                for(Object w: whiteCards){
                    JSONObject white = (JSONObject) w;
                    String cardText = (String) white.get("text");
                    whiteCardTexts.add(cardText);
                }

                // iterate over black cards
                for(Object b: blackCards){
                    JSONObject black = (JSONObject) b;
                    String cardText = (String) black.get("text");
                    blackCardTexts.add(cardText);
                }
            }
        }
        catch (ParseException | IOException e){
            e.printStackTrace();
        }
    }

    // Eliminate black cards with double blanks
    private void eliminateDoubleBlank(){
        blackCardTexts.removeIf(blackCardText -> findOccurrences(blackCardText, '_', 0) > 1);
    }

    // find occurrence of a character in a string recursively
    private int findOccurrences(String str, char search, int index){
        if(index >= str.length()){
            return 0;
        }
        int count = 0;
        if(str.charAt(index) == search)
            count++;

        return count + findOccurrences(str, search, index+1);
    }

    // Public methods to provide with a String text for cards
    public String getWhiteCardText(){
        iteratorWhite++;
        return whiteCardTexts.get(iteratorWhite-1);
    }

    public String getBlackCardText(){
        iteratorBlack++;
        return blackCardTexts.get(iteratorBlack-1);
    }
}