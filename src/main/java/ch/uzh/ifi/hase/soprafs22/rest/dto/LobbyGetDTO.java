package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.User;

public class LobbyGetDTO {

    private Long id;
    private int currentPlayerCount;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {//TEST
        return this.id;
    }

    public void setCurrentPlayerCount(int currentPlayerCount) {
        this.currentPlayerCount = currentPlayerCount;
    }

    public int getCurrentPlayerCount() {
        return this.currentPlayerCount;
    }

}