package ch.uzh.ifi.hase.soprafs22.game.helpers;

import ch.uzh.ifi.hase.soprafs22.entity.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreBoard {
    private User winner;

    public void updateScore(User player, int obtainedPoints){
        //adding the achieved points to the user's score
        int oldScore = player.getScore();
        int newScore = oldScore + obtainedPoints;
        player.setScore(newScore);
    }


    public ArrayList<User> getPlayersOfHighestRank(ArrayList<User> players) {
        //finds the players with the highest score

        /*
         changed return value to an Array of players,
         so that it can be used in the getRanking method and with different rules
         With the rule "the first player, who reaches 5 points, wins", it will just return a list with one player
        */

        ArrayList<User> winners = new ArrayList<>();
        int biggestScore = 0;
        for (User player: players){
            int playerScore = player.getScore();
            if (playerScore == biggestScore){
                //player has the same score as the other players in the winner list
                winners.add(player);
            }
            else if (playerScore > biggestScore){
               // all tentatively winners are getting removed and player is added
                biggestScore = playerScore;
                winners.clear();
                winners.add(player);
            }
        }
        return winners;
    }

    public HashMap getRanking(ArrayList<User> players){
        /*
         not using users as keys as the user can change during the game (e.g. score of user),
         instead using the username as it isn't possible to change it when a game is ongoing
         The rankings of the users are the values of the hashmap
        */

        HashMap<String, Integer> ranking = new HashMap();
        //first all players with first rank gets saved, then all second rank players and so forth
        Integer rank = 1;
        while (!players.isEmpty()){
            ArrayList<User> users = getPlayersOfHighestRank(players);
            for(User user: users){
                ranking.put(user.getUsername(), rank);
                players.remove(user); //removing all user of the same rank from player
            }
            rank ++;
        }
        return ranking;
    }

}
