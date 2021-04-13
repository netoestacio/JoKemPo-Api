package br.com.jokenpo.service;

import br.com.jokenpo.dto.PlayerRequest;
import br.com.jokenpo.dto.PlayerResponse;
import br.com.jokenpo.entity.PlayerEntity;
import br.com.jokenpo.exception.JokenpoException;
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
public class PlayerServiceTest {

    @Autowired
    private PlayerServiceImpl playerService;

    @Test
    public void oneInsertWithSucess() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert just one player
        String expectedPlayerName = "PLAYER NAME";
        PlayerResponse playerResponse = this.playerService.insert(new PlayerRequest(expectedPlayerName));
        Assert.assertEquals(expectedPlayerName, playerResponse.getName());
    }

    @Test(expected = JokenpoException.class)
    public void duplicateInsertForExceptionGenerate() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert two twices the same player name
        String samePlayerName = "DUPLICATED NAME";
        this.playerService.insert(new PlayerRequest(samePlayerName));
        this.playerService.insert(new PlayerRequest(samePlayerName));
    }

    @Test
    public void manyDifferentInsertsWithSuccess() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert many players
        List<String> playerNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5"));
        List<PlayerResponse> playerResponses = this.insertManyDifferentPlayers(playerNames);
        // Assertments check
        int position = 0;
        for (String expectedName : playerNames) {
            Assert.assertEquals(expectedName, playerResponses.get(position).getName());
            position++;
        }
    }

    @Test
    public void getAllPlayersWithSucess() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert many players
        List<String> playerNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5"));
        List<PlayerResponse> playerResponses = this.insertManyDifferentPlayers(playerNames);
        // Assertments check
        Assert.assertEquals(playerNames.size(), playerResponses.size());
        Assert.assertEquals(playerNames.size(), this.playerService.getAll().size());
        Assert.assertEquals(playerResponses.size(), this.playerService.getAll().size());
    }

    @Test
    public void getEntityByNameWithSucess() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert many players
        List<String> playerNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5"));
        List<PlayerResponse> playerResponses = this.insertManyDifferentPlayers(playerNames);
        PlayerEntity entity = this.playerService.getEntityByName("P2");
        // Assertments check
        Assert.assertEquals("P2", entity.getName());
    }

    @Test(expected = JokenpoException.class)
    public void getEntityByNameWithException() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert many players
        List<String> playerNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5"));
        this.insertManyDifferentPlayers(playerNames);
        this.playerService.getEntityByName("INEXISTENTE");
    }

    @Test
    public void deleteByNameWithSucess() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert many players
        List<String> playerNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5"));
        List<PlayerResponse> playerResponse = this.insertManyDifferentPlayers(playerNames);
        int expected1 = playerNames.size()-1;
        int expected2 = playerResponse.size()-1;
        List<PlayerResponse> list = this.playerService.deleteByName("P1");
        // Assertments check
        Assert.assertEquals(expected1, list.size());
        Assert.assertEquals(expected2, list.size());
    }

    @Test(expected = JokenpoException.class)
    public void deleteByNameWithException() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert many players
        List<String> playerNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5"));
        this.insertManyDifferentPlayers(playerNames);
        this.playerService.deleteByName("INEXISTENTE");
    }

    @Test
    public void clearAllWithSucess() throws Exception {
        // Clear singleton data
        this.playerService.clearAll();
        // Insert many players
        List<String> playerNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5"));
        List<PlayerResponse> playerResponse = this.insertManyDifferentPlayers(playerNames);
        Assert.assertEquals(playerNames.size(), playerResponse.size());
        // Clear singleton data
        this.playerService.clearAll();
        // List update
        playerResponse = this.playerService.getAll();
        // Assertments check
        Assert.assertEquals(0, playerResponse.size());
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
