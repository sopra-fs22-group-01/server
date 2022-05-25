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
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

@Service
@Transactional
public class GameService {

    private GameManager gameManager;

    public GameService(GameManager gameManager) {
        this.gameManager = gameManager;
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

    public void removePlayerFromLobby(long lobbyId, long userId) throws Exception {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.removePlayer(userId);

    }

    public boolean checkIfMinimumNumberOfPlayers(long lobbyId) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        return requestedLobby.checkIfEnoughPlayers();
    }

    public boolean checkIfAllPlayersReady(long lobbyId) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        return  requestedLobby.checkIfAllReady();
    }

    public LobbyStatus checkIfLobbyStatusChanged(long lobbyId) throws IncorrectIdException {
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        boolean outcomeMinimumPlayers = checkIfMinimumNumberOfPlayers(lobbyId);
        boolean outcomeReadyPlayers = checkIfAllPlayersReady(lobbyId);
        if (outcomeMinimumPlayers && outcomeReadyPlayers){
            requestedLobby.setLobbyStatus(LobbyStatus.All_Ready);


        }
        else {
            requestedLobby.setLobbyStatus(LobbyStatus.Waiting);
        }
        return requestedLobby.getLobbyStatus();
    }

    public String updateUserReadyStatus(long lobbyId, long userId) throws Exception {
        try {
            Lobby requestedLobby = gameManager.getLobby(lobbyId);
            requestedLobby.setReadyStatus(userId);
            return "Successfully updated readyStatus in Lobby through gameManager";
        } catch (Exception e){
            return "Couldn't update readyStatus in Lobby through gameManager";
        }
    }

    // would actually work with all kinds of updates
    public String updateCustomText(long lobbyId, long userId, UserPutDTO userPutDTO) throws Exception {
        try {
            Lobby requestedLobby = gameManager.getLobby(lobbyId);
            requestedLobby.updateCustomText(userId, userPutDTO);

            return "Successfully updated customText in Lobby through gameManager";
        } catch (Exception e){
            return "Couldn't update readyStatus in Lobby through gameManager";
        }
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

    public void  deleteLobby(long lobbyId)  {
        try {
            Lobby existingLobby = gameManager.getLobby(lobbyId);
            ArrayList <Lobby> lobbies= gameManager.getAllLobbies();
            lobbies.remove(existingLobby);
        }
        catch(IncorrectIdException e1){
            System.out.println("Dont touch it, it works");
        }
    }

    public synchronized Match startMatch(long lobbyId) throws IncorrectIdException {
        //checking if match already created. If yes, return the Match. Else create the Match.
        //if it's already created, you can get the Match with gameManager.getMatch(lobbyId)
        //If not, it will throw an IncorrectIdException("The match was not found")
        try {
            Match existingMatch = gameManager.getMatch(lobbyId);
            return existingMatch;
        }
        catch(IncorrectIdException e1){
            Lobby requestedLobby = gameManager.getLobby(lobbyId);
            Match createdMatch = gameManager.createMatch(requestedLobby.getCurrentPlayers(), lobbyId);
            return createdMatch;
        }
    }

}
