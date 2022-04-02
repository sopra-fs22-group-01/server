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


    public ArrayList<User> getWinner(User[] players) {
        //finds the players with the highest score

        /*changed return value to an Array of players
          as it can happen that more than one player wins the game*/

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

    public HashMap getRanking(User[] players){
        /* not using users as keys as the user can change during the game (e.g. score of user),
         * instead using the username as it isn't possible to change it when a game is ongoing
         * The ranking of the user as value of the hashmap */
        HashMap<String, Integer> ranking = new HashMap();
        for (User player: players){
        }
        return ranking;
    }

}
