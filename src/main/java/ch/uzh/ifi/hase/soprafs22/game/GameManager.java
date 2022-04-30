package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.NoLobbyException;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;


import java.util.ArrayList;

public class GameManager {
    private ArrayList<Lobby> lobbies = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();
    //private long newMatchIdNumber = 0;
    private long newLobbyIdNumber = 0;

    private static GameManager gameManager = null;
    public static synchronized GameManager getInstance(){
        if (gameManager == null){
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public static void resetGameManager(){
        gameManager = null;
    }

    public void createMatch(ArrayList<User> players, Long matchId){
        //new match always gets the id from the lobby
        Match generatedMatch = new Match(matchId);
        //setting the players for the Match
        generatedMatch.setMatchPlayers(players);
        //match doesn't have anymore hands, should create a round whenever a match gets started. The round will create the hands
        generatedMatch.createRound();
        //saving the Match in matches
        generatedMatch.createScoreBoard();
        matches.add(generatedMatch);
    }

    public Lobby createLobby(){
        //generating a unique ID for the Lobby, solved by always increasing the new id
        Long lobbyId = newLobbyIdNumber;
        newLobbyIdNumber++;
        Lobby generatedLobby = new Lobby(lobbyId);
        //saving the Lobby in lobbies
        lobbies.add(generatedLobby);
        return generatedLobby;
    }

    public Lobby getLobby(Long lobbyId) throws IncorrectIdException {
        for (Lobby lobby: lobbies){
            if (lobby.getId().equals(lobbyId)){
                return lobby;
            }
        }
        throw new IncorrectIdException("The lobby was not found");
    }

    public ArrayList<Lobby> getAllLobby() {
        return GameManager.gameManager.lobbies;
    }

    public Match getMatch(Long matchId) throws IncorrectIdException{
        for (Match match: matches){
            if (match.getId() == matchId){
                return match;
            }
        }
        throw new IncorrectIdException("The match was not found");
    }

    /*
    public void deleteLobby(long lobbyId) {
        for (Lobby lobby: lobbies){
            if (lobby.getId() == lobbyId){
                lobbies.remove(lobby);
            }
        }
    }

    public void deleteMatch(long matchId) {
        for (Match match: matches){
            if (match.getId() == matchId){
                matches.remove(match);
            }
        }
    }

    public ArrayList<Long> getLobbiesId() {
        ArrayList<Long> lobbiesId = new ArrayList<>();
        for (Lobby lobby: lobbies){
            lobbiesId.add(lobby.getId());
        }
        return lobbiesId;
    }
    */
}
