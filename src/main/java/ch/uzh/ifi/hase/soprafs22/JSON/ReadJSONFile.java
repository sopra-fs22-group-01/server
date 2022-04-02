package ch.uzh.ifi.hase.soprafs22.JSON;

import org.apache.tomcat.util.json.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadJSONFile {
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("cah-cards-full.json")){
            Object obj = parser.parse(reader);
            JSONArray a = (JSONArray) obj;
            for(Object o: a){
                JSONObject card = (JSONObject) o;
                String text = (String) card.get("text");
                System.out.println("Text: " + text);
            }


        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
}

/*
* json = response.json()
* print(json['items'][0]['volumeInfo']['title'])
* for x in range(nr_books):
*   print(json['items'][x]['volumeInfo']['authors'])
*
* */
