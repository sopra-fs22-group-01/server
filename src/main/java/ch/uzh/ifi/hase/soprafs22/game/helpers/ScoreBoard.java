package ch.uzh.ifi.hase.soprafs22.game.helpers;

import ch.uzh.ifi.hase.soprafs22.entity.User;

import java.util.ArrayList;


public class ScoreBoard {

    public ArrayList<User> getPlayersOfHighestRank(ArrayList<User> players) {
        //finds the players with the highest score

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


    public ArrayList<Ranking> getRanking(ArrayList<User> players){
        /*
         not using users as keys as the user can change during the game (e.g. score of user),
         instead using the username as it isn't possible to change it when a game is ongoing
         The rankings of the users are the values of the hashmap
        */

        //this auxiliary array makes sure we don't delete players from the original player array from match.
        ArrayList<User> tempPlayersCopy = new ArrayList<>();

        for(User player: players){
            tempPlayersCopy.add(player);
        }

        ArrayList<Ranking> rankings = new ArrayList<>();
        //first all players with first rank gets saved, then all second rank players and so forth
        int rank = 1;
        while (!tempPlayersCopy.isEmpty()){
            ArrayList<User> users = getPlayersOfHighestRank(tempPlayersCopy);
            for(User user: users){
                Ranking ranking = new Ranking(rank, user.getUsername(), user.getScore());
                rankings.add(ranking);
                tempPlayersCopy.remove(user); //removing all user of the same rank from player
            }
            rank ++;
        }
        return rankings;
    }
}
