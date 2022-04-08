package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

/*
        // given
        testUser = new User();
        testUser.setId(1L);
        //testUser.setName("testName");
        testUser.setUsername("testUsername");
*/


        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
}

    @Test
    public void getRulesFromText_success() throws Exception {

        ArrayList<String> testArray = gameService.getRulesFromText();
        String expected = "There  are two types of cards: Black cards and white cards.";
        assertEquals(expected,testArray.get(0) );

    }

}
