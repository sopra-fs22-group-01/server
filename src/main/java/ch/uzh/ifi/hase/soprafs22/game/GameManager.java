package ch.uzh.ifi.hase.soprafs22.game;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.exceptions.IncorrectIdException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Scope("singleton") //not strictly needed, since services in spring are singleton per se
@Service
public class GameManager {
    private ArrayList<Lobby> lobbies = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();
    private long newLobbyIdNumber = 0;


    public void reset() {
        this.lobbies.clear();
        this.matches.clear();
        this.newLobbyIdNumber = 0;
    }

    public Match createMatch(ArrayList<User> players, Long matchId) {
        //new match always gets the id from the lobby
        Match generatedMatch = new Match(matchId);
        //setting the players for the Match
        generatedMatch.setMatchPlayers(players);
        //match doesn't have  hands anymore, should create a round whenever a match gets started. The round will create the hands
        generatedMatch.createRound();
        //saving the Match in matches
        generatedMatch.createScoreBoard();
        matches.add(generatedMatch);
        return generatedMatch;
    }

    public Lobby createLobby() {
        //generating a unique ID for the Lobby, solved by always increasing the new id
        Long lobbyId = newLobbyIdNumber;
        newLobbyIdNumber++;
        Lobby generatedLobby = new Lobby(lobbyId);
        //saving the Lobby in lobbies
        lobbies.add(generatedLobby);
        return generatedLobby;
    }

    public Lobby getLobby(Long lobbyId) throws IncorrectIdException {
        for (Lobby lobby : lobbies) {
            if (lobby.getId().equals(lobbyId)) {
                return lobby;
            }
        }
        throw new IncorrectIdException("The lobby was not found");
    }

    public ArrayList<Lobby> getAllLobbies() {
        return this.lobbies;
    }

    public Match getMatch(Long matchId) throws IncorrectIdException {
        for (Match match : matches) {
            if (match.getId().equals(matchId)) {
                return match;
            }
        }
        throw new IncorrectIdException("The match was not found");
    }

    // removes player from all lobbies
    public void removePlayerFromAllLobbies(long userId) throws Exception {
        for (Lobby lobby : this.lobbies){
            for (User user : lobby.getCurrentPlayers()){
                if (user.getId().equals(userId)){
                    lobby.removePlayer(userId);
                    break;
                }
            }
        }
    }


    //checks if leader of match reached max points
    public boolean isGameOver(Match currentMatch) {
        ArrayList<User> players = currentMatch.getMatchPlayers();
        for (User player : players) {
            if (player.getScore() == currentMatch.getRound().getMaxScore()) { //someone reached max score
                return true;
            }
        }
        return false;
    }

    //checks  if new round should be started or nor
    public boolean evaluateNewRoundStart(long matchId) throws IncorrectIdException {
        Match currentMatch = getMatch(matchId);

        //if game is over,returns false
        if (isGameOver(currentMatch)) {
            return false;
        }
        //if game is not over yet, return true (and start new round)

        return true;

    }

    public void startNewRound(long matchId) throws IncorrectIdException {
        Match currentMatch = getMatch(matchId);
        currentMatch.setScoresAlreadyUpdatedFalse();
        currentMatch.resetVotingStatus();
        currentMatch.getRound().startNewRound();
    }

}