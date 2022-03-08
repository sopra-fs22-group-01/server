package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.time.LocalDateTime;

public class UserPostDTO {

  //private String name;

  private String username;

  private String password;

  private LocalDateTime date;

/*  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }*/

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
}
