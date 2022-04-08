package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class GameController {

    private final GameService gameService;

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
}

