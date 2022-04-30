package ch.uzh.ifi.hase.soprafs22.game.helpers;

public class Ranking {

    private int rank;
    private String username;
    private int score;

    public Ranking(int rank, String username) {
        this.rank = rank;
        this.username = username;
    }

    public int getRank() {
        return this.rank;
    }

    public String getUsername() {
        return this.username;
    }


    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public int getScore() {
        return this.score;
    }
}
