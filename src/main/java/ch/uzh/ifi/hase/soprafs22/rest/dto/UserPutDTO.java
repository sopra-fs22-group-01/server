package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.util.Date;

public class UserPutDTO {
//private String name;

    private String username;

    private String password;

    private long id;

    private Date date;

    private UserStatus userStatus;

    private boolean isLoggedIn;

    private Date birthday;



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

    public Date getDate(){return date;}

    public void setDate(Date date){this.date = date;}

    public void setIsLoggedIn(boolean isLoggedIn){
        this.isLoggedIn = isLoggedIn;
    }

    public boolean getIsLoggedIn(){
        return this.isLoggedIn;
    }

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
}
