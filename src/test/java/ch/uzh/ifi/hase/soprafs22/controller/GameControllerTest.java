package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.Round;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Ranking;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.VotingStatus;
import ch.uzh.ifi.hase.soprafs22.rest.dto.WhiteCardPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Lob;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

   /* @MockBean
    private UserService userService;
*/

    @MockBean
    public GameManager gameManager;



    @Test
    public void getRules_success() throws Exception {

        //this won't return anything, since the gameService is mocked
        //ArrayList<String> expectedArrayList = gameService.getRulesFromText();

        ArrayList<String> expectedArrayList = new ArrayList<>();
        expectedArrayList.add("There  are two types of cards: Black cards and white cards.");


        given(gameService.getRulesFromText()).willReturn(expectedArrayList);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/rules")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn();
        String expected = "[\"There  are two types of cards: Black cards and white cards.\"]";
        assertEquals(expected, result.getResponse().getContentAsString());
    }


    @Test
    public void getLobbyStatus_success() throws Exception {
        //given

        Lobby testLobby = new Lobby(0L);
        testLobby.setLobbyStatus(LobbyStatus.All_Ready);

        //given = when from springboot
        given(gameService.getLobbyStatus(testLobby.getId())).willReturn(testLobby.getLobbyStatus());


        MockHttpServletRequestBuilder getRequest = get("/lobbies/" + testLobby.getId() + "/status")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("All_Ready"));
    }


    @Test
    public void getLobbyStatus_lobby_not_found() throws Exception {


        //given defines what the mocked class should return when calling a specific method

        given(gameService.checkIfLobbyStatusChanged(Mockito.anyLong())).willThrow(new IncorrectIdException("The lobby was not found"));
        //given(gameService.getLobbyStatus(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "No lobby with this id could be found."));

        MockHttpServletRequestBuilder getRequest = get("/lobbies/" + anyLong() + "/status")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isConflict())
                .andReturn();


        assertTrue(result.getResolvedException().getMessage().contains("No lobby with this id could be found."));
    }

    @Test
    void createNewLobby_returns_ok() throws Exception {

        //
        Lobby testLobby = new Lobby(1L);
        //converts long to int, since ResponseEntity seems to convert long to int
        int expected = Math.toIntExact(testLobby.getId());


        given(gameService.createNewLobby()).willReturn(testLobby);


        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expected)));

    }


    //ADD assertions
    @Test
    void getAllLobbies_returns_ok() throws Exception {

        //given
        Lobby testLobby = new Lobby(0L);
        Lobby testLobby2 = new Lobby(1L);
        ArrayList<Lobby> lobbies = new ArrayList<>();
        lobbies.add(testLobby);
        lobbies.add(testLobby2);


        given(gameManager.getAllLobbies()).willReturn(lobbies);

        MockHttpServletRequestBuilder getRequest = get("/lobbies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

    }

    @Test
    void getAllUsersByLobbyId_success() throws Exception {

        //given
        int user1Id = 0;

        Lobby testLobby = new Lobby(0L);
        testLobby.setId(0L);
        User user1 = createTestUser(user1Id);
        User user2 = createTestUser(1L);
        testLobby.addPlayer(user1);
        testLobby.addPlayer(user2);
        ArrayList<User> allTestUsers = new ArrayList<>();
        allTestUsers.add(user1);
        allTestUsers.add(user2);


        given(gameManager.getLobby(0L)).willReturn(testLobby);


        MockHttpServletRequestBuilder getRequest = get("/lobbies/" + testLobby.getId() + "/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user1Id))) //longValue needed
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getAllUsersByLobbyId_throwsException() throws Exception {

        given(gameManager.getLobby(0L)).willThrow(new IncorrectIdException("The lobby was not found"));


        MockHttpServletRequestBuilder getRequest = get("/lobbies/" + Mockito.anyLong() + "/users")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        assertTrue(result.getResolvedException().getMessage().contains("Couldn't retrieve lobby users"));
    }


    @Test
    void getRoundNumberSpecificMatch_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();
        Round testRound = testMatch.getRound();
        int roundNumber = testRound.getRoundNumber();

        given(gameManager.getMatch(0L)).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + testMatch.getId() + "/roundnumbers")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk());
    }


    @Test
    void getAllUsersFromSpecificMatch_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();

        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + testMatch.getId() + "/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(0))) //longValue needed
                .andExpect(jsonPath("$[0].username",is(testPlayer.getUsername())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getBlackCard() throws Exception {
        BlackCard testBlackCard = new BlackCard();
        testBlackCard.createCard();


        given(gameService.getBlackCard(Mockito.anyLong())).willReturn(testBlackCard);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + Mockito.anyLong() + "/blackCard")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(testBlackCard.getText()));
    }



    //put
    @Test
    void incrementWhiteCard_success() throws Exception {
        int id = 0;

        //the way we have to handle void methods
        doNothing().when(gameService).incrementCardScore(id, id);

        MockHttpServletRequestBuilder putRequest = put("/matches/" + Mockito.anyLong() + "/white-cards/" + Mockito.anyLong())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print()); // used to log the perform

    }

    @Test
    void incrementWhiteCard_shouldThrowException() throws Exception {
        int id = 0;

        //the way we have to handle void methods
        doThrow(new IncorrectIdException("The match was not found")).when(gameService).incrementCardScore(id, id);

        MockHttpServletRequestBuilder putRequest = put("/matches/" + Mockito.anyLong() + "/white-cards/" + Mockito.anyLong())
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(putRequest)
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print())
                .andReturn(); // used to log the perform

        assertTrue(result.getResolvedException().getMessage().contains("Wrong ID, Couldn't retrieve the match"));
    }


    @Test
    void getSelectionCountdown_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();

        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + testMatch.getId() + "/countdown/selection")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    void getVotingCountdown_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();

        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + testMatch.getId() + "/countdown/voting")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }


    @Test
    void getRankingCountdown_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();

        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + testMatch.getId() + "/countdown/roundwinners")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }


    @Test
    void startSelectionCountdown_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();

        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder putRequest = put("/matches/" + testMatch.getId() + "/countdown/selection")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    void startVotingCountdown_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();

        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder putRequest = put("/matches/" + testMatch.getId() + "/countdown/voting")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    void startRankingCountdown_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();

        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder putRequest = put("/matches/" + testMatch.getId() + "/countdown/roundwinners")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }


    @Test
    void addChosenCard_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();
        Round testRound = testMatch.getRound();
        testRound.startNewRound();

        WhiteCard whiteCard= new WhiteCard();
        WhiteCardPutDTO whiteCardPutDTO= new WhiteCardPutDTO();
        whiteCardPutDTO.setId(whiteCard.getId());
        whiteCardPutDTO.setOwner(testPlayer);
        whiteCardPutDTO.setText(whiteCard.getText());
        whiteCard.setScore(0);


        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder putRequest = put("/matches/" + testMatch.getId() + "/white-card/selection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(whiteCardPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }


    @Test
    void getChosenCards_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player1");
        Match testMatch = new Match(0L);
        ArrayList<User> matchPlayers = new ArrayList<>();
        matchPlayers.add(testPlayer);
        testMatch.setMatchPlayers(matchPlayers);
        testMatch.createRound();
        Round testRound = testMatch.getRound();
        testRound.startNewRound();

        WhiteCard whiteCard= new WhiteCard();
        whiteCard.setOwner(testPlayer);
        whiteCard.setId(0L);


        testRound.setChosenCard(whiteCard);


        given(gameManager.getMatch(testMatch.getId())).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + testMatch.getId() + "/election/white-cards")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text",is(whiteCard.getText())));
    }


    @Test
    void getRankingOfAllPlayers_success() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player");
        testPlayer.setScore(1);
        ArrayList<User> testMatchPlayers = new ArrayList<>();
        testMatchPlayers.add(testPlayer);
        Match testMatch = new Match(0L);
        testMatch.setMatchPlayers(testMatchPlayers);
        testMatch.createRound();

        ScoreBoard testScoreBoard = new ScoreBoard();
        ArrayList<Ranking> ranking = testScoreBoard.getRanking(testMatchPlayers);



        given(gameService.getRanking(anyLong())).willReturn(ranking);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + 0 + "/scores")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rank",is(1)))
                .andExpect(jsonPath("$[0].username",is(testPlayer.getUsername())))
                .andExpect(jsonPath("$[0].score",is(testPlayer.getScore())));

    }


    @Test
    void getRankingOfAllPlayers_throwsError() throws Exception {

        given(gameService.getRanking(anyLong())).willThrow(new IncorrectIdException("The match was not found"));

        MockHttpServletRequestBuilder getRequest = get("/matches/" + anyLong() + "/scores")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isConflict())
                .andReturn();

        assertTrue(result.getResolvedException().getMessage().contains("Wrong ID, Couldn't retrieve the match"));
    }



    @Test
    void getWinningCards_success() throws Exception { //returns the winner cards
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player");
        testPlayer.setScore(1);
        ArrayList<User> testMatchPlayers = new ArrayList<>();
        testMatchPlayers.add(testPlayer);
        Match testMatch = new Match(0L);
        testMatch.setMatchPlayers(testMatchPlayers);
        testMatch.createRound();

        WhiteCard whiteCard= new WhiteCard();
        whiteCard.createCard();
        whiteCard.setOwner(testPlayer);
        whiteCard.setId(0L);
        Round testRound = testMatch.getRound();
        testRound.setChosenCard(whiteCard);
        ArrayList<WhiteCard> winningCards = new ArrayList<>();
        winningCards.add(whiteCard);


        given(gameManager.getMatch(anyLong())).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/" + anyLong() + "/winner")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text",is(whiteCard.getText())));
    }

    @Test
    void getWinningCards_throwsIncorrectIdException() throws Exception { //returns the winner cards

        given(gameManager.getMatch(anyLong())).willThrow(new IncorrectIdException("The match was not found"));

        MockHttpServletRequestBuilder getRequest = get("/matches/" + anyLong() + "/winner")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isConflict())
                .andReturn();

        assertTrue(result.getResolvedException().getMessage().contains("Wrong ID, Couldn't retrieve the winner"));
    }


    @Test
    void incrementApiRequestCount() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player");
        testPlayer.setScore(1);
        ArrayList<User> testMatchPlayers = new ArrayList<>();
        testMatchPlayers.add(testPlayer);
        Match testMatch = new Match(0L);
        testMatch.setMatchPlayers(testMatchPlayers);
        testMatch.createRound();




        //the way we have to handle void methods
        given(gameManager.getMatch(0L)).willReturn(testMatch);

        MockHttpServletRequestBuilder putRequest = put("/matches/"+0L+"/synchronization")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

        assertEquals(VotingStatus.COMPLETE,testMatch.getVotingStatus());
    }

    @Test
    void getApiRequestStatus() throws Exception {
        User testPlayer = new User();
        testPlayer.setId(0L);
        testPlayer.setUsername("player");
        testPlayer.setScore(1);
        ArrayList<User> testMatchPlayers = new ArrayList<>();
        testMatchPlayers.add(testPlayer);
        Match testMatch = new Match(0L);
        testMatch.setMatchPlayers(testMatchPlayers);
        testMatch.createRound();

        //the way we have to handle void methods
        given(gameManager.getMatch(0L)).willReturn(testMatch);

        MockHttpServletRequestBuilder getRequest = get("/matches/"+0L+"/synchronization")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();


        assertTrue(result.getResponse().getContentAsString().contains("INCOMPLETE"));
    }

@Disabled
    @Test
    void deleteUserFromLobby_success() throws Exception { //returns the winner cards

        doNothing().when(gameService).removePlayerFromLobby(0,0);

        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/" + anyLong() + "/players/" + anyLong())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    void deleteUserFromLobby_throwsIncorrectIdException() throws Exception { //returns the winner cards

        doThrow(new IncorrectIdException("The lobby was not found")).when(gameService).removePlayerFromLobby(anyLong(),anyLong());

        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/" + anyLong() + "/players/" + anyLong())
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(deleteRequest)
                .andExpect(status().isConflict())
                .andReturn();

        assertTrue(result.getResolvedException().getMessage().contains("No lobby with this id could be found."));
    }

    @Disabled
    @Test
    void deleteUserFromLobby_throwsException() throws Exception { //returns the winner cards

        doThrow(new Exception("The lobby was not found")).when(gameService).removePlayerFromLobby(anyLong(),anyLong());

        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/" + anyLong() + "/players/" + anyLong())
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue(result.getResolvedException().getMessage().contains("No such user exists in the lobby"));
    }






/*
    //create match successful --> check same Id, same players as lobby, matchStatus
    @Test // endpoint location: GameController line 129
    public void givenLobbyId_createNewMatch_successful() throws Exception {
        // simulate all steps before creating match

    // create user
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setUserStatus(UserStatus.ONLINE);
        given(userService.createUser(Mockito.any())).willReturn(user);

    // create lobby
        Lobby lobby = new Lobby(0L);
        given(gameService.createNewLobby()).willReturn(lobby);

        long lobbyId = lobby.getId();

    // create new match and add users to it
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);

        Match match = new Match(lobbyId);
        match.setMatchPlayers(userList);
    // starting match should return match with: players, id, matchStatus, round
        given(gameService.startMatch(Mockito.anyLong())).willReturn(match);

    // build request
        MockHttpServletRequestBuilder postRequest = post("/matches/"+lobbyId);

        // then perform request
        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.matchStatus", is(match.getMatchStatus().toString())))
                .andExpect(jsonPath("$.matchPlayers[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$.id", is(match.getId().intValue())))
                .andExpect(jsonPath("$.laughStatus", is(match.getLaughStatus())))
                .andExpect(jsonPath("$.available_Supervotes", is(match.getAvailable_Supervotes())));
        //@Mapping(source = "laughStatus", target = "laughStatus")
        //    @Mapping(source = "available_Supervotes", target = "available_Supervotes")

    }
*/

    //helper functions

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }


    private User createTestUser(long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
