package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class RankingGetDTO {

    private int rank;
    private String username;

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
