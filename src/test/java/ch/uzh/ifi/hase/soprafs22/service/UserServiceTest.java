package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceTest {


    @Mock
    private  GameManager gameManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        //testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("testToken");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
     void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);
        createdUser.setUserStatus(UserStatus.OFFLINE);
        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE,createdUser.getUserStatus()); //but should it be true though?
    }

 /* @Test
  public void createUser_duplicateName_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    //Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }*/

    @Test
     void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        // Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    void logOutUserSuccess(){

        testUser.setUserStatus(UserStatus.ONLINE);
        userService.logOutUser(testUser);

        assertEquals(UserStatus.OFFLINE,testUser.getUserStatus());
    }

    @Test
    void loggInUserSuccess(){

        testUser.setUserStatus(UserStatus.OFFLINE);
        userService.logInUser(testUser);

        assertEquals(UserStatus.ONLINE,testUser.getUserStatus());
    }


    @Test
    void findUserDataSuccess(){

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        User sameUser = userService.findUserData(testUser.getId());

        assertEquals(testUser,sameUser);
    }

    @Test
    void findUserDataThrowsError()  {

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(null);

        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
                    userService.findUserData(testUser.getId());},"");

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),thrown.getRawStatusCode());

    }

    @Test
    void findUserByToken_success(){
        Mockito.when(userRepository.findByToken(Mockito.anyString())).thenReturn(testUser);

        User userByToken = userService.findUserByToken(testUser.getToken());

        assertEquals(testUser,userByToken);
    }

    @Test
    void findUserByToken_exception(){
        Mockito.when(userRepository.findByToken(Mockito.anyString())).thenReturn(null);

        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.findUserByToken(testUser.getToken());},"");

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),thrown.getRawStatusCode());
    }

    @Test
    void findUserById_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        User userById = userService.findUserById(testUser.getId());

        assertEquals(testUser,userById);
    }

    @Test
    void findUserById_exception(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(null);

        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.findUserById(testUser.getId());},"");

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),thrown.getRawStatusCode());
    }

    @Test
    void updateUserUsername_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        UserPutDTO testUserPutDto = new UserPutDTO();
        testUserPutDto.setUsername("newName");
        testUserPutDto.setId(testUser.getId());

        userService.updateUserUsername(testUserPutDto);

        assertEquals(testUserPutDto.getUsername(),testUser.getUsername());
    }

    @Test
    void updateUserPassword_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        UserPutDTO testUserPutDto = new UserPutDTO();
        testUserPutDto.setPassword("newPassword");
        testUserPutDto.setId(testUser.getId());

        userService.updateUserPassword(testUserPutDto);

        assertEquals(testUserPutDto.getPassword(),testUser.getPassword());
    }

    @Test
    void updateUserReadyStatus_fromReady_to_Unready_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        testUser.setIsReady(ReadyStatus.READY);

        userService.updateUserReadyStatus(testUser);

        assertEquals(ReadyStatus.UNREADY, testUser.getIsReady());
    }

    @Test
    void updateUserReadyStatus_fromUnready_to_Ready_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        testUser.setIsReady(ReadyStatus.UNREADY);

        userService.updateUserReadyStatus(testUser);

        assertEquals(ReadyStatus.READY, testUser.getIsReady());
    }

    @Test
    void resetUserReadyStatus_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        testUser.setIsReady(ReadyStatus.READY);

        userService.resetUserReadyStatus(testUser);

        assertEquals(ReadyStatus.UNREADY, testUser.getIsReady());
    }

    @Test
    void resetUserScore_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        testUser.setScore(3);

        userService.resetUserScore(testUser);

        assertEquals(0,testUser.getScore());
    }

    @Test
    void updateCustomWhiteText_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        testUser.setCustomWhiteText("testText");
        UserPutDTO testUserPutDto = new UserPutDTO();
        testUserPutDto.setCustomWhiteText("newCustomText");
        testUserPutDto.setId(testUser.getId());

        userService.updateCustomWhiteText(testUserPutDto);

        assertEquals(testUserPutDto.getCustomWhiteText(),testUser.getCustomWhiteText());
    }

    @Test
    void updateUser_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        UserPutDTO testUserPutDto = new UserPutDTO();
        testUserPutDto.setUsername("newName");
        testUserPutDto.setId(testUser.getId());
        testUserPutDto.setPassword("newPassword");

        userService.updateUser(testUserPutDto);

        assertEquals(testUserPutDto.getUsername(),testUser.getUsername());
        assertEquals(testUserPutDto.getPassword(),testUser.getPassword());
    }


    @Test
    void updateScores_success(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        ArrayList<WhiteCard> whiteCards = new ArrayList<>();
        WhiteCard testWhiteCard = new WhiteCard();
        testWhiteCard.createCard();
        testWhiteCard.setScore(1);
        testWhiteCard.setOwner(testUser);
        whiteCards.add(testWhiteCard);

        assertEquals(0,testUser.getScore());

        userService.updateScores(whiteCards);

        assertEquals(1,testUser.getScore());
    }

    @Test
    void incrementPlayedGames(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        testUser.setPlayedGames(0);

        userService.incrementPlayedGames(testUser.getId());

        assertEquals(1,testUser.getPlayedGames());
    }

    @Test
    void setSuperVote_Success(){
        List<User> testPlayers = new ArrayList<>();
        testUser.setSuperVote(0);
        testPlayers.add(testUser);


        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        userService.setSuperVotes(testPlayers,1);

        assertEquals(1,testUser.getSuperVote());
    }

    @Test
    void updateSuperVote_Success(){
        testUser.setSuperVote(1);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        userService.updateSupervote(testUser.getId());

        assertEquals(0,testUser.getSuperVote());
    }

    @Test
    void updateSuperVote_ThrowsException(){
        testUser.setSuperVote(0);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            userService.updateSupervote(testUser.getId());},"");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),thrown.getRawStatusCode());
    }


    @Test
    void updateOverallWins_success() throws IncorrectIdException {

        ArrayList<User> players = new ArrayList<>();
        players.add(testUser);
        Match testMatch = new Match(0L);
        testMatch.setMatchPlayers(players);
        testMatch.createScoreBoard();
        testUser.setScore(2);
        testUser.setOverallWins(0);

        Mockito.when(gameManager.getMatch(Mockito.anyLong())).thenReturn(testMatch);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(testUser);

        userService.updateOverallWins(testUser.getId(), testMatch.getId());

        assertEquals(1,testUser.getOverallWins());
    }


}
