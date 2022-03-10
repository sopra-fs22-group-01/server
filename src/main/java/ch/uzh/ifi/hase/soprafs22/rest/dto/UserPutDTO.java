package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.time.LocalDateTime;

public class UserPutDTO {
//private String name;

    private String username;

    private String password;

    private LocalDateTime date;

    private UserStatus status;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return password;}

    public void setPassword(String password){
        this.password = password;
    }

    public LocalDateTime getDate(){return date;}

    public void setDate(LocalDateTime date){this.date = date;}

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

}