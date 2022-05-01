package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @AfterEach
  public void tearDown(){
    GameManager.resetGameManager();
  }

  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setPassword("testName");
    testUser.setUsername("testUsername");

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));



    User testUser = new User();
    testUser.setUsername("testUsername");
    testUser.setPassword("testPassword");
    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the name but forget about the username
    testUser2.setUsername("testUsername");
    testUser2.setPassword(("testPassword"));
    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }


  @Test
  public void multipleUsers_login_joinLobby_success() throws Exception {
    //setUp
    GameService testGameService = new GameService();
    Lobby testLobby = testGameService.createNewLobby();

    //create first user
    User testUser1 = new User();
    testUser1.setUsername("testUsername1");
    testUser1.setPassword("testPassword");
    testUser1.setId(1l);
    testUser1.setIsReady(ReadyStatus.UNREADY);
    userService.createUser(testUser1);

    //create second user
    User testUser2 = new User();
    testUser2.setUsername("testUsername2");
    testUser2.setPassword("testPassword");
    testUser2.setId(2l);
    testUser2.setIsReady(ReadyStatus.UNREADY);
    userService.createUser(testUser2);

    //add user1 and user 2 to lobby
    testGameService.addPlayerToLobby(testLobby.getId(),testUser1);
    testGameService.addPlayerToLobby(testLobby.getId(),testUser2);
    testGameService.checkIfLobbyStatusChanged(testLobby.getId());

    //check lobby status before players are all ready
    LobbyStatus beforeReady = testGameService.getLobbyStatus(testLobby.getId());
    LobbyStatus expectedLobbyStatusBefore = LobbyStatus.Waiting;
    assertEquals(expectedLobbyStatusBefore,beforeReady);

    //should change users ready status
    testGameService.updateUserReadyStatus(testLobby.getId(),1l);
    testGameService.updateUserReadyStatus(testLobby.getId(),2l);
    testGameService.checkIfLobbyStatusChanged(testLobby.getId());

    //check lobby status after players are all ready
    LobbyStatus actualAfterUpdateReadyStatus = testGameService.getLobbyStatus(testLobby.getId());
    LobbyStatus expectedLobbyStatusAfter = LobbyStatus.All_Ready;
    assertEquals(expectedLobbyStatusAfter,actualAfterUpdateReadyStatus);

  }

  /*@Test
  public void allUsers_ready_then_createMatch_success() throws Exception {
    GameManager.resetGameManager();
    GameManager.getInstance();
    //setUp
    GameService testGameService = new GameService();
    Lobby testLobby = testGameService.createNewLobby();
    *//*testGameService.startMatch(testLobby.getId());*//*

    //create first user
    User testUser1 = new User();
    testUser1.setUsername("testUsername1");
    testUser1.setPassword("testPassword");
    testUser1.setId(1l);
    testUser1.setIsReady(ReadyStatus.UNREADY);
    userService.createUser(testUser1);

    //create second user
    User testUser2 = new User();
    testUser2.setUsername("testUsername2");
    testUser2.setPassword("testPassword");
    testUser2.setId(2l);
    testUser2.setIsReady(ReadyStatus.UNREADY);
    userService.createUser(testUser2);

    //add user1 and user 2 to lobby
    testGameService.addPlayerToLobby(testLobby.getId(),testUser1);
    testGameService.addPlayerToLobby(testLobby.getId(),testUser2);
    testGameService.checkIfLobbyStatusChanged(testLobby.getId());

    //should change users ready status
    testGameService.updateUserReadyStatus(testLobby.getId(),1l);
    testGameService.updateUserReadyStatus(testLobby.getId(),2l);
    testGameService.checkIfLobbyStatusChanged(testLobby.getId());

    //should create a match with the same id as the lobby the players come from
    GameManager testGameManager = GameManager.getInstance();
    Match testMatch = testGameService.startMatch(testLobby.getId());


    //since the match MUST have the same id as the lobby, we check
    //if we find the match by lobbyId
    Match actualMatch = testGameManager.getMatch(testLobby.getId());
    assertEquals(testLobby.getId(),actualMatch.getId());
  }*/
}
