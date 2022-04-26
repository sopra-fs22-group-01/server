package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
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

    public void updateUserReadyStatus(long lobbyId, long userId, ReadyStatus readyStatus) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.setReadyStatus(userId, readyStatus);
    }

    public void startMatch(long lobbyId) throws IncorrectIdException{
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.setGamePlayers();
        //delete of the lobby after entering the corresponding match
        gameManager.deleteLobby(lobbyId);
        Match match = gameManager.getMatch(lobbyId); //the started match from the lobby has the same id
        match.createHands();
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

    // NOT COMPLETE -> doesn't account for matchId yet
    public static String getBlackCard(Long matchId){
        BlackCard black = new BlackCard();
        black.createCard();
        return black.getText();
    }
}
