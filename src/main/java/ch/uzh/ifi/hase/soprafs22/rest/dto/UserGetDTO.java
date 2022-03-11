package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.time.LocalDateTime;

public class UserGetDTO {

  private Long id;
  private String username;
  private String token;
  private String password;
  private LocalDateTime date;
  private boolean isLoggedIn;

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

  public void setIsLoggedIn(boolean isLoggedIn) {
    this.isLoggedIn = isLoggedIn;
  }

  public void setToken(String token){
    this.token = token;
  }

  public String getToken(String token){
    return this.token;
  }

}

