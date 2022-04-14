package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    public void addPlayerToLobby(long lobbyId, User user) throws Exception {
        GameManager gameManager = GameManager.getInstance();
        Lobby requestedLobby = gameManager.getLobby(lobbyId);
        requestedLobby.addPlayer(user);
    }



}
