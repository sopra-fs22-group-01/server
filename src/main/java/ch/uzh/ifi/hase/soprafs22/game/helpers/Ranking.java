package ch.uzh.ifi.hase.soprafs22.game.helpers;

public class Ranking {

    private int rank;
    private String username;

    public Ranking(int rank, String username) {
        this.rank = rank;
        this.username = username;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }


    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
