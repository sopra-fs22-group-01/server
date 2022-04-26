package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;

import java.util.ArrayList;

public class Lobby {

    private Long id;
    private LobbyStatus lobbyStatus;
    private ArrayList<User> currentPlayers = new ArrayList<>();
    private final int minimumNumberOfPlayers = 5;

    public Lobby(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LobbyStatus getLobbyStatus() {
        return lobbyStatus;
    }

    public void setLobbyStatus(LobbyStatus lobbyStatus) {
        this.lobbyStatus = lobbyStatus;
    }

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
        if (!currentPlayers.contains(user)) {
            currentPlayers.add(user);
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
        if (currentPlayers.size() >= minimumNumberOfPlayers) {
            return true;
        }
        return false;
    }

    public void setGamePlayers() {
        GameManager gameManager = GameManager.getInstance();
        //the belonging match to the lobby has the same id as the lobby
        gameManager.createMatch(currentPlayers, id);
    }

    public void setReadyStatus(long userId, ReadyStatus readyStatus) {
        for (User player : currentPlayers) {
            if (player.getId().equals(userId)) {
                player.setIsReady(readyStatus);
            }
        }
    }
}