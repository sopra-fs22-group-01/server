package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
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
    //public void setCurrentPlayerCount(int currentPlayerCount){this.currentPlayerCount=currentPlayerCount;}


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

    public int addPlayer(User user) throws Exception {
        if (!currentPlayers.contains(user) && currentPlayerCount<=maximumPlayerCount) {
            currentPlayers.add(user);
            currentPlayerCount++;
            return currentPlayerCount;
        }
        else {
            throw new Exception("The user is already in the lobby");
        }
    }

    public void removePlayer(long userId) throws Exception {
        for (User player: currentPlayers){
            if(player.getId().equals(userId)){
                currentPlayers.remove(player);
                currentPlayerCount--;
                return;
            }
        }
        throw new Exception("The user doesn't exist in the lobby");
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
        gameManager.createMatch(this.currentPlayers, this.id);
    }

    public void setReadyStatus(long userId) {
        for (User player : currentPlayers) {
            if (player.getId().equals(userId)) {
                if (player.getIsReady().equals(ReadyStatus.READY)) {
                    player.setIsReady(ReadyStatus.UNREADY);
                }

                else if (player.getIsReady().equals(ReadyStatus.UNREADY)) {
                    player.setIsReady(ReadyStatus.READY);
                }
            }
        }
    }

    public void updateCustomText(long userId, UserPutDTO userPutDTO){
        ReadyStatus readyStatus = userPutDTO.getIsReady();
        String customText = userPutDTO.getCustomWhiteText();

        for (User user: this.currentPlayers){
            if (user.getId().equals(userId)){
                /*if (readyStatus != null){
                    user.setIsReady(readyStatus);
                }*/
                if (customText != null){
                    user.setCustomWhiteText(customText);
                }
            }
        }
    }


}