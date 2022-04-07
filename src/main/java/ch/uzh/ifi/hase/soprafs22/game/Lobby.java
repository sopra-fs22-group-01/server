package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;

import java.util.ArrayList;

public class Lobby{

    private ArrayList<User> currentPlayers = new ArrayList<>();
    private final int minimumNumberOfPlayers = 5;


    public boolean checkIfAllReady(ArrayList<User> players){
        for (User player: players){
            if (player.getIsReady().equals(ReadyStatus.UNREADY)){
                return false;
            }
        }
        return true;
    }

    public void addPlayers(User user){
        currentPlayers.add(user);
    }

    public void removePlayer(User user){
        if(currentPlayers.contains(user)){
            currentPlayers.remove(user);
        }
    }

    public boolean checkIfEnoughPlayers(){
        if(currentPlayers.size() >= minimumNumberOfPlayers){
            return true;
        }
        return false;
    }


    public void setGamePlayers(){
        Game game = Game.getInstance();
        game.setGamePlayers(currentPlayers);

    }
}