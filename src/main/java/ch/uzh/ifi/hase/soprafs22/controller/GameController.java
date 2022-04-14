package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class GameController {

    private final GameService gameService;
    //GameManager gameManager = GameManager.getInstance()

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/rules")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ArrayList<String>> getRules() throws Exception {

        ArrayList<String> rules = gameService.getRulesFromText();

        //.ok sets the HTTP status to OK (200)
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/matches/{matchId}/scores")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<HashMap>getScores() throws Exception{
        //Match match = gameManager.getMatch(matchId);
        HashMap scoresHashMap = new HashMap();
        return ResponseEntity.ok(scoresHashMap);
    }
}

