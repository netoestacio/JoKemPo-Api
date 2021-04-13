package br.com.jokenpo.controller;

import br.com.jokenpo.dto.MoveRequest;
import br.com.jokenpo.dto.MoveResponse;
import br.com.jokenpo.dto.PlayerRequest;
import br.com.jokenpo.dto.PlayerResponse;
import br.com.jokenpo.dto.api.ApiResponse;
import br.com.jokenpo.enumeration.EnumMovement;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.repository.MoveRepository;
import br.com.jokenpo.repository.PlayerRepository;
import br.com.jokenpo.service.PlayerService;
import br.com.jokenpo.service.impl.MoveServiceImpl;
import br.com.jokenpo.service.impl.PlayerServiceImpl;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
public class MoveControllerTest {

    private static String HOST = "http://localhost";
    private static String ENDPOINT = "/v1/btg/jokenpo/move";

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
    public void getAllWithoutAnyMovementInsertedAPI() throws URISyntaxException {
        // Adjust players and movements
        this.playerService.clearAll();
        this.moveService.clearAll();
        // Get all movements
        ResponseEntity<String> result = restTemplate.getForEntity(getMovementsUri(), String.class);
        // Verify request
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("data"));
        Assert.assertEquals(false, result.getBody().contains("player"));
        Assert.assertEquals(false, result.getBody().contains("movement"));
        // Convert
        ApiResponse apiResponse = new Gson().fromJson(result.getBody(), ApiResponse.class);
        List<MoveResponse> listResponse = new ModelMapper().map(apiResponse.getData(), List.class);
        // Assertments check
        Assert.assertEquals(0, listResponse.size());
    }

    @Test
    public void insertMovementAPI() throws URISyntaxException, JokenpoException {
        // Adjust player
        this.playerService.clearAll();
        this.playerService.insert(new PlayerRequest("P2"));
        // Request object
        MoveRequest moveRequest = new MoveRequest("P2", EnumMovement.PAPER.getName());
        HttpEntity<MoveRequest> requestForInsert = new HttpEntity<>(moveRequest, new HttpHeaders());
        // Post for insert
        ResponseEntity<String> result = restTemplate.postForEntity(getMovementsUri(), requestForInsert, String.class);
        // Assertments check
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("data"));
        Assert.assertEquals(true, result.getBody().contains("movement"));
        // Assertment check
        Assert.assertNotEquals(0, this.getAllMovements().size());
    }

    @Test
    public void deleteMovementByNameAPI() throws URISyntaxException, JokenpoException {
        // Adjust player and movement
        this.playerService.clearAll();
        this.moveService.clearAll();
        this.playerService.insert(new PlayerRequest("P3"));
        this.moveService.insert(new MoveRequest("P3", EnumMovement.SPOCK.getName()));
        // Delete one movement
        restTemplate.delete(getMovementsUri() + "/?playerName=P3");
        // Assertment check
        Assert.assertEquals(0, this.getAllMovements().size());
    }

    private List<MoveResponse> getAllMovements() throws URISyntaxException {
        // Get all movements
        ResponseEntity<String> result = restTemplate.getForEntity(getMovementsUri(), String.class);
        ApiResponse apiResponse = new Gson().fromJson(result.getBody(), ApiResponse.class);
        // Convert to moveResponse list
        return new ModelMapper().map(apiResponse.getData(), List.class);
    }

    private URI getMovementsUri() throws URISyntaxException {
        final String baseUrl = HOST + ":" + randomServerPort + ENDPOINT;
        return new URI(baseUrl);
    }

}