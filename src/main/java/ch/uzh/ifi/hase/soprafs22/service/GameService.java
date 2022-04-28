package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.Round;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);




    private static final GameManager gameManager = GameManager.getInstance();

    /*@Autowired //what does @Autowired do exactly?
    public GameService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }*/



    //reads Rules textfile from game/helpers/rules
    public ArrayList<String> getRulesFromText() throws Exception{
        ArrayList<String> ruleArrayList = new ArrayList<>();


        try{
            File file = new File("src/main/java/ch/uzh/ifi/hase/soprafs22/game/helpers/rules.txt");
            Scanner sc = new Scanner(file);

            while(sc.hasNextLine()){
                ruleArrayList.add(sc.nextLine());
            }


        }
        catch (Exception e){
            System.out.println("file not found");
        }

    return ruleArrayList;


    }

   ////NOW IN USERSERVICE
  /*  public User findUserById(long id) {
        User requestedUser = userRepository.findById(id);
        if (requestedUser==null){
            String baseErrorMessage = "User not found!";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage));
        }
        return requestedUser;
    }*/

    public void addPlayerToLobby(long lobbyId, User user) throws Exception {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.addPlayer(user);
    }

    public void removePlayerFromLobby(long lobbyId, User user) throws Exception {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.removePlayer(user);
    }

    public boolean checkIfMinimumNumberOfPlayers(long lobbyId) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        return requestedLobby.checkIfEnoughPlayers();
    }

    public boolean checkIfAllPlayersReady(long lobbyId) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        return  requestedLobby.checkIfAllReady();
    }

    public void checkIfLobbyStatusChanged(long lobbyId) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        boolean outcomeMinimumPlayers = checkIfMinimumNumberOfPlayers(lobbyId);
        boolean outcomeReadyPlayers = checkIfAllPlayersReady(lobbyId);
        if (outcomeMinimumPlayers && outcomeReadyPlayers){
            requestedLobby.setLobbyStatus(LobbyStatus.All_Ready);
        }
        else {
            requestedLobby.setLobbyStatus(LobbyStatus.Waiting);
        }
    }

    public void updateUserReadyStatus(long lobbyId, long userId) throws Exception {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.setReadyStatus(userId);
    }

    public void startMatch(long lobbyId) throws IncorrectIdException{
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.createMatchWithPlayers();
    }

    public LobbyStatus getLobbyStatus(long lobbyId) throws IncorrectIdException{
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        return requestedLobby.getLobbyStatus();
    }
    /*
    public LobbyStatus getLobbyStatus() {
        List<User> users = userRepository.findAll();
        for (User user : users){
            if (user.getIsReady()== ReadyStatus.UNREADY){
                return LobbyStatus.Waiting;
            }
        }
        return LobbyStatus.All_Ready;
    }
    */

    // retrieve blackCard by matchId
    public BlackCard getBlackCard(Long matchId) throws IncorrectIdException {

        Match currentMatch = gameManager.getMatch(matchId);
        BlackCard blackCard = currentMatch.getRound().getBlackCard();

        return blackCard;
    }
    public void incrementCardScore (long matchId, long searchedCardOwnerId) throws Exception { //cardOwnerId
        Match match = gameManager.getMatch(matchId);
        Round currentRound = match.getRound();
        ArrayList<WhiteCard> allChosenCards = currentRound.getAllChosenCards();

        //iterates through all chosen cards and increments the wanted card by 1
        for(WhiteCard whiteCard : allChosenCards){
            // in chosenCard is max. one card per player as each player can only choose one card
            // therefore it's possible to identify the card with the owner's ID
            long cardOwnerId = whiteCard.getOwner().getId();
            if(cardOwnerId == searchedCardOwnerId){
                whiteCard.incrementCard(); //increments the card score by 1
            }
        }
    }

    public ArrayList<Long> getLobbiesId() {
        return gameManager.getLobbiesId();
    }

    public Lobby createNewLobby() {
        Lobby createdLobby = gameManager.createLobby();
        return createdLobby;
    }


    public ArrayList<Lobby> getAllLobbies() {
        ArrayList<Lobby> allLobbies = gameManager.getAllLobby();
        return allLobbies;
    }

    public HashMap<String, Integer> getRanking(long matchId) throws IncorrectIdException {
        Match match = gameManager.getMatch(matchId);
        ScoreBoard scoreBoard = match.getScoreBoard();
        ArrayList<User> matchPlayers = match.getMatchPlayers();
        HashMap<String, Integer> ranking = scoreBoard.getRanking(matchPlayers);
        return ranking;
    }

}
