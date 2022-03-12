package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.time.LocalDateTime;
import java.util.Date;

public class UserPostDTO {

  //private String name;

  private String username;

  private String password;

  private LocalDateTime date;

  private boolean isLoggedIn;
  private String token;
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

  public LocalDateTime getDate(){return date;}

  public void setDate(LocalDateTime date){this.date = date;}

  public boolean getIsLoggedIn(){
    return this.isLoggedIn;
  }
  public void setIsLoggedIn(boolean isLoggedIn){
    this.isLoggedIn = isLoggedIn;
  }

  public void setToken(String token){
    this.token = token;
  }

  public String getToken(String token){
    return this.token;
  }


  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

}
