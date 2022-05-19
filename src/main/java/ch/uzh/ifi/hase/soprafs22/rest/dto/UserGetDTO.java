package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.util.Date;

public class UserGetDTO {

    private Long id;
    private String username;
    private String token;
    private String password;
    private Date date;
    private ReadyStatus isReady;
    private UserStatus userStatus;
    private Date birthday;
    private int score;
    private String customWhiteText;
    private int superVote;
    private int overallWins;
    private int playedGames;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public ReadyStatus getIsReady() {
        return isReady;
    }
    public void setIsReady(ReadyStatus readyStatus) {
        this.isReady = readyStatus;
    }

    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }

    public String getCustomWhiteText() {
        return this.customWhiteText;
    }
    public void setCustomWhiteText(String customWhiteText) {
        this.customWhiteText = customWhiteText;
    }

    public int getSuperVote(){
        return this.superVote;
    }
    public void setSuperVote(int superVote){
        this.superVote = superVote;
    }

    public int getOverallWins() { return this.overallWins;}
    public void setOverallWins(int overallWins) {this.overallWins = overallWins;}

    public int getPlayedGames() { return this.playedGames;}
    public void setPlayedGames(int playedGames) { this.playedGames = playedGames;}

}

