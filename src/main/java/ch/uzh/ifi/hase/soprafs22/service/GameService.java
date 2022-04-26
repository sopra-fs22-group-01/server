package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ReadyStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.Round;
import ch.uzh.ifi.hase.soprafs22.game.card.BlackCard;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.LobbyStatus;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private GameManager gameManager = GameManager.getInstance();


    @Autowired //what does @Autowired do exactly?
    public GameService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //reads Rules textfile from game/helpers/rules
    public ArrayList<String> getRulesFromText() throws Exception{
        ArrayList<String> ruleArrayList = new ArrayList<String>();


        try{
            File file = new File("src/main/java/ch/uzh/ifi/hase/soprafs22/game/helpers/rules.txt");
            Scanner sc = new Scanner(file);

            while(sc.hasNextLine()){
                ruleArrayList.add(sc.nextLine());
            }


        }
        catch (Exception e){
            System.out.println("file not found");
        }

    return ruleArrayList;


    }

    public LobbyStatus getLobbyStatus() {
        List<User> users = userRepository.findAll();
        for (User user : users){
            if (user.getIsReady()== ReadyStatus.UNREADY){
                return LobbyStatus.Waiting;
            }
        }
        return LobbyStatus.All_Ready;
    }

    // NOT COMPLETE -> doesn't account for matchId yet
    public static String getBlackCard(Long matchId){
        BlackCard black = new BlackCard();
        black.createCard();
        return black.getText();
    }


    public void incrementCardScore (long matchId, long cardId) throws Exception {
        Match match = gameManager.getMatch(matchId);
        Round currentRound = match.getRound();
        ArrayList<WhiteCard> allChosenCards = currentRound.getAllChosenCards();

        //iterates through all chosen cards and increments the wanted card by 1
        for(WhiteCard card : allChosenCards){
            if(card.getId() == cardId){
                card.incrementCard(); //increments the card score by 1
            }
        }

    }


}
