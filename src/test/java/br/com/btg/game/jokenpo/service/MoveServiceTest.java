package br.com.jokenpo.service;

import br.com.jokenpo.dto.MoveRequest;
import br.com.jokenpo.dto.MoveResponse;
import br.com.jokenpo.dto.PlayerRequest;
import br.com.jokenpo.dto.PlayerResponse;
import br.com.jokenpo.enumeration.EnumMovement;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.service.impl.MoveServiceImpl;
import br.com.jokenpo.service.impl.PlayerServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MoveServiceTest {

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private MoveServiceImpl moveService;

    @Test
    public void insertManyPlayersForTestWithSucess() throws JokenpoException {
        // Clear singleton data
        this.playerService.clearAll();
        this.moveService.clearAll();
        // Insert many players
        List<String> playerNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5", "P6"));
        List<PlayerResponse> playerResponse = this.insertManyDifferentPlayers(playerNames);
        // Assertments check
        Assert.assertEquals(playerNames.size(), playerResponse.size());
    }

    @Test
    public void playersWithoutMovements() throws JokenpoException {
        this.insertManyPlayersForTestWithSucess();
        int playersCounter = this.playerService.getAll().size();
        int movementsCounter = this.moveService.getAll().size();
        // Assertments check
        Assert.assertEquals(0, movementsCounter);
        Assert.assertNotEquals(0, playersCounter);
    }

    @Test
    public void insertOneMovement() throws JokenpoException {
        this.insertManyPlayersForTestWithSucess();
        int expected = 1;
        MoveResponse response = this.moveService.insert(new MoveRequest("P1", "STONE"));
        // Assertments check
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getMovement());
        Assert.assertNotNull(response.getPlayer());
        Assert.assertEquals(expected, this.moveService.getAll().size());
    }

    @Test(expected = JokenpoException.class)
    public void insertDuplicatedMovementForSamePlayerWithException() throws JokenpoException {
        this.insertManyPlayersForTestWithSucess();
        this.moveService.insert(new MoveRequest("P1", EnumMovement.STONE.getName()));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.LIZARD.getName()));
    }

    @Test(expected = JokenpoException.class)
    public void insertDuplicatedMovementForSamePlayerAndSameMovementWithException() throws JokenpoException {
        this.insertManyPlayersForTestWithSucess();
        this.moveService.insert(new MoveRequest("P1", EnumMovement.STONE.getName()));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.STONE.getName()));
    }

    @Test
    public void insertMovementForDifferentPlayersWithSucess() throws JokenpoException {
        this.insertManyPlayersForTestWithSucess();
        this.moveService.insert(new MoveRequest("P1", EnumMovement.STONE.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.LIZARD.getName()));
        this.moveService.insert(new MoveRequest("P3", EnumMovement.STONE.getName()));
        this.moveService.insert(new MoveRequest("P4", EnumMovement.SCISSORS.getName()));
        this.moveService.insert(new MoveRequest("P5", EnumMovement.PAPER.getName()));
        // Assertments check
        Assert.assertEquals(5, this.moveService.getAll().size());
    }

    @Test
    public void deleteOneMovementWithSucess() throws JokenpoException {
        this.insertManyPlayersForTestWithSucess();
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SPOCK.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.STONE.getName()));
        this.moveService.insert(new MoveRequest("P3", EnumMovement.SCISSORS.getName()));
        int beforeCounter = this.moveService.getAll().size();
        this.moveService.deleteByPlayerName("P2");
        // Assertments check
        Assert.assertEquals(beforeCounter-1, this.moveService.getAll().size());
    }

    @Test
    public void deleteOneMovementAfterInsertAnotherWithSucess() throws JokenpoException {
        this.insertManyPlayersForTestWithSucess();
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SPOCK.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.STONE.getName()));
        this.moveService.insert(new MoveRequest("P3", EnumMovement.SCISSORS.getName()));
        this.moveService.deleteByPlayerName("P2");
        this.moveService.insert(new MoveRequest("P2", EnumMovement.PAPER.getName()));
        // Assertments check
        Assert.assertEquals(3, this.moveService.getAll().size());
    }

    @Test
    public void clearAllMovementsWithSucess() throws JokenpoException {
        this.insertManyPlayersForTestWithSucess();
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SPOCK.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.STONE.getName()));
        this.moveService.insert(new MoveRequest("P3", EnumMovement.SCISSORS.getName()));
        Assert.assertNotEquals(0, this.moveService.getAll().size());
        this.moveService.clearAll();
        Assert.assertEquals(0, this.moveService.getAll().size());
    }

    private List<PlayerResponse> insertManyDifferentPlayers(List<String> playerNames) throws JokenpoException {
        List<PlayerResponse> list = new ArrayList<>();
        for(String name : playerNames){
            PlayerResponse playerResponse = this.playerService.insert(new PlayerRequest(name));
            list.add(playerResponse);
        }
        return list;
    }

}
