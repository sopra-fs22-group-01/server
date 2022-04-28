package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

public class Lobby {

    private Long id;
    private LobbyStatus lobbyStatus;
    private ArrayList<User> currentPlayers = new ArrayList<>();
    private int currentPlayerCount=0;
    private final int maximumPlayerCount = 5;

    //constructor
    public Lobby(Long id) {
        this.id = id;
    }

    //getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {this.id = id;}

    public int getCurrentPlayerCount() {return currentPlayerCount;}
    public void setCurrentPlayerCount(int currentPlayerCount){this.currentPlayerCount=currentPlayerCount;}


    public LobbyStatus getLobbyStatus() {
        return lobbyStatus;
    }
    public void setLobbyStatus(LobbyStatus lobbyStatus) {this.lobbyStatus = lobbyStatus;}

    public ArrayList<User> getCurrentPlayers(){return this.currentPlayers;}

    public boolean checkIfAllReady() {
        for (User player : currentPlayers) {
            if (player.getIsReady().equals(ReadyStatus.UNREADY)) {
                return false;
            }
        }
        return true;
    }

    public void addPlayer(User user) throws Exception {
        if (!currentPlayers.contains(user) && currentPlayerCount<=maximumPlayerCount) {
            currentPlayers.add(user);
            currentPlayerCount++;
        }
        else {
            throw new Exception("The user is already in the lobby");
        }
    }

    public void removePlayer(User user) throws Exception {
        if (currentPlayers.contains(user)) {
            currentPlayers.remove(user);
        }
        else {
            throw new Exception("The user doesn't exist in the lobby");
        }
    }

    public boolean checkIfEnoughPlayers() {
        if (currentPlayers.size() >0) {
            return true;
        }
        return false;
    }

    public void createMatchWithPlayers() {
        GameManager gameManager = GameManager.getInstance();
        //the belonging match to the lobby has the same id as the lobby
        gameManager.createMatch(currentPlayers, id);
    }

    public void setReadyStatus(long userId) {
        /*
        for (User player : currentPlayers) {
            if (player.getId().equals(userId)) {
                player.setIsReady(ReadyStatus.READY);
            }
            else{
                String baseErrorMessage = "Couldn't change the status";
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format(baseErrorMessage, "username ", "is"));
            }
         */
        for (User player : currentPlayers) {
            if (player.getId().equals(userId)) {
                if (player.getIsReady().equals(ReadyStatus.READY)) {
                    player.setIsReady(ReadyStatus.UNREADY);
                    //return true;
                }

                else if (player.getIsReady().equals(ReadyStatus.UNREADY)) {
                    player.setIsReady(ReadyStatus.READY);
                    //return true;
                }
            }
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}