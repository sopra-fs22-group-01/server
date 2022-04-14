package ch.uzh.ifi.hase.soprafs22.game;

import java.util.ArrayList;

public class GameManager {
    private ArrayList<Lobby> lobbies = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();

    public Lobby getLobby(Long lobbyId) throws Exception {
        for (Lobby lobby: lobbies){
            if (lobby.getId() == lobbyId){
                return lobby;
            }
        }
        throw new Exception("The lobby was not found");
    }

    public Match getMatch(Long matchId){
        return null;
    }
}
