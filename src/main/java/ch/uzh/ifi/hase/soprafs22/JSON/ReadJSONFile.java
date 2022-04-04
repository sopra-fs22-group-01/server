/**
 * This class is merely for reading JSON Against Humanity file
 * It creates cards instances out of JSON Objects
 *
 * Complexity: O(n^2)
 *
 * Problems:
 * 2-Static Method
 * */

package ch.uzh.ifi.hase.soprafs22.JSON;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
* Don't create instances, create an array list and a method which returns a randomized string (cardText)
* */

public class ReadJSONFile {

    public static void main(String[] args) {
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

                    System.out.println(white.get("text"));
                    String cardText = (String) white.get("text");
                    //WhiteCard whiteCard = new WhiteCard(cardText);

                }

                // iterate over black cards
                for(Object b: blackCards){
                    JSONObject black = (JSONObject) b;
                    System.out.println((String) black.get("text"));
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}