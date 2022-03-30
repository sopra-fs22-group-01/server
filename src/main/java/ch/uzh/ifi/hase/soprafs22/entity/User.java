package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.game.Hand;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String token;


  @Column(nullable = false)
  private String password;

  @Column
  private Date creation_date;

  @Column
  private boolean isLoggedIn;

  @Column
  private Date birthday;

  //For the game isReady, score and hand was added
  @Column
  private ReadyStatus isReady;

  @Column
  private int score;

  /*
  doesn't work as Hand isn't a basic type
  @Column
  private Hand hand;
  */

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

  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  public void setPassword(String password){
      this.password = password;
  }

  public String getPassword(){
      return password;
  }

  public Date getCreation_date(){return creation_date;}

  public void setCreation_date(Date date){
    this.creation_date = date;

  }

  public boolean getIsLoggedIn(){
    return this.isLoggedIn;
  }
  public void setIsLoggedIn(boolean isLoggedIn){
    this.isLoggedIn = isLoggedIn;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }
}
