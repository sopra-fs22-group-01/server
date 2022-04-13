package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.util.Date;

public class UserPutDTO {
//private String name;

    private String username;

    private String password;

    private long id;

    private Date date;

    private UserStatus userStatus;


    private Date birthday;
    private ReadyStatus isReady;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password){
        this.password = password;
    }


    public void setDate(Date date){this.date = date;}


    public void setId(long id){
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public ReadyStatus getIsReady() {
        return isReady;
    }

    public String getPassword(){return password;}

    public void setIsReady(ReadyStatus readyStatus) {this.isReady = readyStatus;}
}
