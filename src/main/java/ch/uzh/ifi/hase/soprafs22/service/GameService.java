package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.Round;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Ranking;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

@Service
@Transactional
public class GameService {

    //private final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final GameManager gameManager = GameManager.getInstance();



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

    public void updateUserReadyStatus(long lobbyId, long userId) throws Exception {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.setReadyStatus(userId);
    }

    public Match startMatch(long lobbyId) throws IncorrectIdException{
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        return gameManager.createMatch(requestedLobby.getCurrentPlayers(), lobbyId);
        //requestedLobby.createMatchWithPlayers();
    }

    public LobbyStatus getLobbyStatus(long lobbyId) throws IncorrectIdException{
        Lobby requestedLobby = gameManager.getLobby(lobbyId);

        return requestedLobby.getLobbyStatus();
    }

    // retrieve blackCard by matchId
    public BlackCard getBlackCard(Long matchId) throws IncorrectIdException {

        Match currentMatch = gameManager.getMatch(matchId);
        BlackCard blackCard = currentMatch.getRound().getBlackCard();

        return blackCard;
    }

    public void incrementCardScore (long matchId, long searchedCardOwnerId) throws IncorrectIdException {
        Match match = gameManager.getMatch(matchId);
        Round currentRound = match.getRound();
        currentRound.incrementCardScores(searchedCardOwnerId);
    }

    public Lobby createNewLobby() {
        Lobby createdLobby = gameManager.createLobby();
        return createdLobby;
    }

    public ArrayList<Ranking> getRanking(long matchId) throws IncorrectIdException {
        Match match = gameManager.getMatch(matchId);
        ScoreBoard scoreBoard = match.getScoreBoard();
        ArrayList<User> matchPlayers = match.getMatchPlayers();
        ArrayList<Ranking> ranking = scoreBoard.getRanking(matchPlayers);
        return ranking;
    }

    /*
    public ArrayList<Lobby> getAllLobbies() {
        ArrayList<Lobby> allLobbies = gameManager.getAllLobby();
        return allLobbies;
    }

    public ArrayList<Long> getLobbiesId() {
        return gameManager.getLobbiesId();
    }
    */

}
