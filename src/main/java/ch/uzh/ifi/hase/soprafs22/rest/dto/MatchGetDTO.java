package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.LaughStatus;
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
    private LaughStatus laughStatus;
    private int available_Supervotes;

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

    public int getAvailable_Supervotes() {return available_Supervotes;}
    public void setAvailable_Supervotes(int available_Supervotes) {this.available_Supervotes = available_Supervotes;}

    public LaughStatus getLaughStatus() {return laughStatus;}
    public void setLaughStatus(LaughStatus laughStatus) {this.laughStatus = laughStatus;}


}

