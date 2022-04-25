package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private GameManager gameManager = GameManager.getInstance();

    @Autowired //what does @Autowired do exactly?
    public GameService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //reads Rules textfile from game/helpers/rules
    public ArrayList<String> getRulesFromText() throws Exception{
        ArrayList<String> ruleArrayList = new ArrayList<String>();


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

    public void addPlayerToLobby(long lobbyId, User user) throws Exception {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.addPlayer(user);
    }

    public void removePlayerFromLobby(long lobbyId, User user) throws Exception{
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.removePlayer(user);
    }

    public boolean checkIfMinimumNumberOfPlayers(long lobbyId) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        boolean result = requestedLobby.checkIfEnoughPlayers();
        return result;
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

    public void updateUserReadyStatus(long lobbyId, long userId, ReadyStatus readyStatus) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.setReadyStatus(userId, readyStatus);
    }

    public void startMatch(long lobbyId) throws IncorrectIdException{
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.setGamePlayers();
        Match match = gameManager.getMatch(requestedLobby.getId()); //the started match from the lobby has the same id
        match.createHands();
    }

    public LobbyStatus getLobbyStatus(long lobbyId) throws IncorrectIdException{
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        return requestedLobby.getLobbyStatus();
    }
}
