package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.game.GameManager;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.helpers.ScoreBoard;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class GameController {

    private final GameService gameService;
    GameManager gameManager = GameManager.getInstance();

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
    public ResponseEntity<HashMap>getScores(@PathVariable long matchId) throws Exception{
  /*      Match match = gameManager.getMatch(matchId);//gets correct match from gameManager
        ScoreBoard currentScoreBoard = match.getScoreBoard();//gets scoreboard of correct match

        //gets hashmap which contains player name as key and player score as value
        HashMap scoresHashMap = currentScoreBoard.getCurrentScores(match.getGamePlayers());
        */

        // JUST A MOCK! Doesnt work now, since no match gets initialized until now
        HashMap hardCodedMap = new HashMap();
        hardCodedMap.put("Player1", 1);

        return ResponseEntity.ok(hardCodedMap);
    }
}

