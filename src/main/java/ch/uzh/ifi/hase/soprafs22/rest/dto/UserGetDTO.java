package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

public class UserGetDTO {

  private Long id;
  private String name;
  private String username;
  private UserStatus status;
  private String password;
  private String date;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

 /* public String getName() {
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

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public String getPassword(){
      return password;}

  public void setPassword(String password){
      this.password = password;
    }

  public String getDate(){return date;}

  public void setDate(String date){this.date = date;}

}
