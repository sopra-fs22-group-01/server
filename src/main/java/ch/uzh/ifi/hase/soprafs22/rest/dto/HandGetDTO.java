package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.Hand;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;

import java.util.ArrayList;
import java.util.Date;

public class HandGetDTO {//TEST

    private ArrayList<WhiteCard> userHand = new ArrayList<WhiteCard>();
    private WhiteCard chosenCard;
    private User owner;

    public void setHand(ArrayList<WhiteCard> hand){
        this.userHand = hand;
    }
    public ArrayList<WhiteCard> getHand(){
        return userHand;
    }

    public void setChosenCard(WhiteCard card){
        this.chosenCard = card;
    }
    public WhiteCard getChosenCard(){
        return this.chosenCard;
    }

    public void setOwner(User user){
        this.owner = user;
    }
    public User getOwner(){
        return this.owner;
    }
}

