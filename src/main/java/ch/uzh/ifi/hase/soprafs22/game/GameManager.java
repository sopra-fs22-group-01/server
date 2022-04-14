package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;

import java.util.ArrayList;

public class GameManager {
    private ArrayList<Lobby> lobbies = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();
    private long newMatchIdNumber = 0;
    private long newLobbyIdNumber = 0;

    private static GameManager gameManager = null;
    public static GameManager getInstance(){
        if (gameManager == null){
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public void createMatch(ArrayList<User> players){
        //generating a unique ID for the Match, solved by always increasing the new id
        Long matchId = newMatchIdNumber;
        newMatchIdNumber ++;
        Match generatedMatch = new Match(matchId);
        //setting the players for the Match
        generatedMatch.setMatchPlayers(players);
        //saving the Match in matches
        matches.add(generatedMatch);
    }

    public void createLobby(){
        //generating a unique ID for the Lobby, solved by always increasing the new id
        Long lobbyId = newLobbyIdNumber;
        newLobbyIdNumber++;
        Lobby generatedLobby = new Lobby(lobbyId);
        //saving the Lobby in lobbies
        lobbies.add(generatedLobby);
    }

    public Lobby getLobby(Long lobbyId) throws IncorrectIdException {
        for (Lobby lobby: lobbies){
            if (lobby.getId() == lobbyId){
                return lobby;
            }
        }
        throw new IncorrectIdException("The lobby was not found");
    }

    public Match getMatch(Long matchId) throws IncorrectIdException{
        for (Match match: matches){
            if (match.getId() == matchId){
                return match;
            }
        }
        throw new IncorrectIdException("The match was not found");
    }
}
