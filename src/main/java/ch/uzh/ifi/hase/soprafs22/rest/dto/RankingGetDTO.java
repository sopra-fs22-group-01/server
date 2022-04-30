package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class RankingGetDTO {

    private int rank;
    private String username;
    private int score;

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public void setRank(int rank) {this.rank = rank;}
    public int getRank() {return rank;}

    public void setScore(int score) {this.score = score;}
    public int getScore() {return this.score;}
}
