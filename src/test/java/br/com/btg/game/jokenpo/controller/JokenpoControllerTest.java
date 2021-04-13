package br.com.jokenpo.controller;

import br.com.jokenpo.dto.JokenpoResponse;
import br.com.jokenpo.dto.MoveRequest;
import br.com.jokenpo.dto.PlayerRequest;
import br.com.jokenpo.dto.PlayerResponse;
import br.com.jokenpo.dto.api.ApiResponse;
import br.com.jokenpo.enumeration.EnumMovement;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.repository.MoveRepository;
import br.com.jokenpo.repository.PlayerRepository;
import br.com.jokenpo.service.impl.MoveServiceImpl;
import br.com.jokenpo.service.impl.PlayerServiceImpl;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class JokenpoControllerTest {

    private static String HOST = "http://localhost";
    private static String ENDPOINT = "/v1/btg/jokenpo/play";

    @LocalServerPort
    private int randomServerPort;

    private RestTemplate restTemplate;
    private PlayerRepository playerRepository;
    private MoveRepository moveRepository;
    private MoveServiceImpl moveService;
    private PlayerServiceImpl playerService;

    @Before
    public void setup(){
        restTemplate = new RestTemplate();
        playerRepository = new PlayerRepository();
        moveRepository = new MoveRepository();
        moveService = new MoveServiceImpl(moveRepository, playerRepository);
        playerService = new PlayerServiceImpl(playerRepository, moveService);
    }

    @Test
    public void playWithNobodyWonAPI() throws URISyntaxException, JokenpoException {
        // Insert players and movements
        this.playerService.clearAll();
        this.moveService.clearAll();
        this.playerService.insert(new PlayerRequest("P1"));
        this.playerService.insert(new PlayerRequest("P2"));
        this.playerService.insert(new PlayerRequest("P3"));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SPOCK.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.SCISSORS.getName()));
        this.moveService.insert(new MoveRequest("P3", EnumMovement.PAPER.getName()));
        // Play
        ResponseEntity<String> result = restTemplate.getForEntity(getJokenpoUri(), String.class);
        // Verify request
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("data"));
        Assert.assertEquals(true, result.getBody().contains("history"));
        Assert.assertEquals(true, result.getBody().contains("result"));
        Assert.assertEquals(true, result.getBody().contains("NOBODY WON"));
    }

    @Test
    public void playWithWinnerAPI() throws URISyntaxException, JokenpoException {
        // Insert players and movements
        this.playerService.clearAll();
        this.moveService.clearAll();
        this.playerService.insert(new PlayerRequest("P1"));
        this.playerService.insert(new PlayerRequest("P2"));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SCISSORS.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.PAPER.getName()));
        // Play
        ResponseEntity<String> result = restTemplate.getForEntity(getJokenpoUri(), String.class);
        // Verify request
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("data"));
        Assert.assertEquals(true, result.getBody().contains("history"));
        Assert.assertEquals(true, result.getBody().contains("result"));
        Assert.assertEquals(true, result.getBody().contains("P1 IS THE WINNER"));
    }

    @Test
    public void clearAllAPI() throws URISyntaxException, JokenpoException {
        // Insert players and movements
        this.playerService.clearAll();
        this.moveService.clearAll();
        this.playerService.insert(new PlayerRequest("P1"));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.PAPER.getName()));
        // Assertment check
        Assert.assertEquals(1, this.playerService.getAll().size());
        Assert.assertEquals(1, this.moveService.getAll().size());
        // Clear all by API call
        restTemplate.delete(getJokenpoUri() + "/?playerName=P1");
        // Assertment check
        Assert.assertEquals(0, this.playerService.getAll().size());
        Assert.assertEquals(0, this.moveService.getAll().size());
    }

    private URI getJokenpoUri() throws URISyntaxException {
        final String baseUrl = HOST + ":" + randomServerPort + ENDPOINT;
        return new URI(baseUrl);
    }

}