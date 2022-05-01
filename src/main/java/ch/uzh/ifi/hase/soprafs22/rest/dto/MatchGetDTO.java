package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.MatchStatus;
import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;

import java.util.ArrayList;
import java.util.Date;

public class MatchGetDTO {

    private Long id;
    private MatchStatus matchStatus;
    private ArrayList<User> matchPlayers;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public MatchStatus getMatchStatus() {
        return this.matchStatus;
    }
    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public ArrayList<User> getMatchPlayers() {
        return this.matchPlayers;
    }
    public void setMatchPlayers(ArrayList<User> players) {this.matchPlayers = players;}

}

