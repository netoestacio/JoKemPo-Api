package br.com.jokenpo.controller;

import br.com.jokenpo.dto.PlayerRequest;
import br.com.jokenpo.dto.PlayerResponse;
import br.com.jokenpo.dto.api.ApiResponse;
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
public class PlayerControllerTest {

    private static String HOST = "http://localhost";
    private static String ENDPOINT = "/v1/btg/jokenpo/player";

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
    public void getAllWithoutAnyPlayersInsertedAPI() throws URISyntaxException {
        // Adjust players
        this.playerService.clearAll();
        // Get all players
        ResponseEntity<String> result = restTemplate.getForEntity(getPlayerUri(), String.class);
        // Verify request
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("data"));
        // Convert to ApiResponse
        ApiResponse apiResponse = new Gson().fromJson(result.getBody(), ApiResponse.class);
        // Assertments check
        Assert.assertNotNull(apiResponse.getMeta().getTimestamp());
        // Convert to playerResponse list
        List<PlayerResponse> listResponse = new ModelMapper().map(apiResponse.getData(), List.class);
        // Assertments check
        Assert.assertEquals(0, listResponse.size());
    }

    @Test
    public void insertPlayerAPI() throws URISyntaxException {
        // Adjust players
        this.playerService.clearAll();
        // Inser request
        HttpEntity<PlayerRequest> requestForInsert = new HttpEntity<>(
                new PlayerRequest("P1"), new HttpHeaders());
        // Post for insert
        ResponseEntity<String> result = restTemplate.postForEntity(getPlayerUri(), requestForInsert, String.class);
        // Assertments check
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("data"));
        Assert.assertEquals(true, result.getBody().contains("playerName"));
        Assert.assertEquals(true, result.getBody().contains("P1"));
        // Assertment check
        Assert.assertEquals(1, this.getAllPlayers().size());
    }

    @Test
    public void deletePlayerByNameAPI() throws URISyntaxException, JokenpoException {
        // Adjust players
        this.playerService.clearAll();
        this.playerService.insert(new PlayerRequest("P1"));
        // Delete one player
        restTemplate.delete(getPlayerUri() + "/?playerName=P1");
        // Assertment check
        Assert.assertEquals(0, this.getAllPlayers().size());
    }

    private List<PlayerResponse> getAllPlayers() throws URISyntaxException {
        // Get all players
        ResponseEntity<String> result = restTemplate.getForEntity(getPlayerUri(), String.class);
        ApiResponse apiResponse = new Gson().fromJson(result.getBody(), ApiResponse.class);
        // Convert to playerResponse list
        return new ModelMapper().map(apiResponse.getData(), List.class);
    }

    private URI getPlayerUri() throws URISyntaxException {
        final String baseUrl = HOST + ":" + randomServerPort + ENDPOINT;
        return new URI(baseUrl);
    }

}