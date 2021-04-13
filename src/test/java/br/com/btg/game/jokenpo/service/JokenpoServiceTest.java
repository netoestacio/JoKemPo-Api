package br.com.jokenpo.service;

import br.com.jokenpo.dto.*;
import br.com.jokenpo.enumeration.EnumMovement;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.service.impl.JokenpoServiceImpl;
import br.com.jokenpo.service.impl.MoveServiceImpl;
import br.com.jokenpo.service.impl.PlayerServiceImpl;
import org.junit.*;
import org.junit.rules.ExpectedException;
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
public class JokenpoServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private MoveServiceImpl moveService;

    @Autowired
    private JokenpoServiceImpl jokenpoService;

    @Before
    public void setup(){
        this.clearAllData();
    }

    @Test
    public void clearAllDataWithSucess() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2", "P3", "P4", "P5", "P6"));
        // Movements insert
        insertSomeMovements(
            Arrays.asList(
                new MoveRequest("P1", EnumMovement.SPOCK.getName()),
                new MoveRequest("P2", EnumMovement.PAPER.getName()),
                new MoveRequest("P3", EnumMovement.SCISSORS.getName())
            )
        );
        // Assertments check
        Assert.assertNotEquals(0, this.playerService.getAll().size());
        Assert.assertNotEquals(0, this.moveService.getAll().size());
        this.jokenpoService.clear();
        Assert.assertEquals(0, this.playerService.getAll().size());
        Assert.assertEquals(0, this.moveService.getAll().size());
    }

    @Test
    public void paperVersusScissorsPlaying() throws JokenpoException {
        // Players insert
        this.insertSomePlayers(Arrays.asList("P1", "P2"));
        // Movements insert
        insertSomeMovements(
            Arrays.asList(
                new MoveRequest("P1", EnumMovement.PAPER.getName()),
                new MoveRequest("P2", EnumMovement.SCISSORS.getName())
            )
        );
        // Action
        JokenpoResponse response = this.jokenpoService.play();
        // Assertments check
        Assert.assertNotNull(response.getGameResult());
        String expected = "P2 IS THE WINNER!".toUpperCase().trim();
        Assert.assertEquals(expected, response.getGameResult());
    }

    @Test
    public void paperVersusScissorsVersusStonePlaying() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2", "P3"));
        // Movements insert
        insertSomeMovements(
            Arrays.asList(
                new MoveRequest("P1", EnumMovement.PAPER.getName()),
                new MoveRequest("P2", EnumMovement.SCISSORS.getName()),
                new MoveRequest("P3", EnumMovement.STONE.getName())
            )
        );
        // Action
        JokenpoResponse response = this.jokenpoService.play();
        // Assertments check
        Assert.assertNotNull(response.getGameResult());
        String expected = "NOBODY WON!".toUpperCase().trim();
        Assert.assertEquals(expected, response.getGameResult());
    }

    @Test
    public void lizardVersusScissorsVersusPaperPlaying() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2", "P3"));
        // Movements insert
        insertSomeMovements(
            Arrays.asList(
                new MoveRequest("P1", EnumMovement.LIZARD.getName()),
                new MoveRequest("P2", EnumMovement.SCISSORS.getName()),
                new MoveRequest("P3", EnumMovement.PAPER.getName())
            )
        );
        // Action
        JokenpoResponse response = this.jokenpoService.play();
        // Assertments check
        Assert.assertNotNull(response.getGameResult());
        String expected = "P2 IS THE WINNER!".toUpperCase().trim();
        String notExpected = "NOBODY WON!".toUpperCase().trim();
        Assert.assertNotEquals(notExpected, response.getGameResult());
        Assert.assertEquals(expected, response.getGameResult());
    }

    @Test
    public void spockVersusPaperPlaying() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2"));
        // Movements insert
        insertSomeMovements(
                Arrays.asList(
                        new MoveRequest("P1", EnumMovement.SPOCK.getName()),
                        new MoveRequest("P2", EnumMovement.PAPER.getName())
                )
        );
        // Action
        JokenpoResponse response = this.jokenpoService.play();
        // Assertments check
        Assert.assertNotNull(response.getGameResult());
        String expected = "P2 IS THE WINNER!".toUpperCase().trim();
        String notExpected = "NOBODY WON!".toUpperCase().trim();
        Assert.assertNotEquals(notExpected, response.getGameResult());
        Assert.assertEquals(expected, response.getGameResult());
    }

    @Test
    public void lizardVersusScissorsPlaying() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2"));
        // Movements insert
        insertSomeMovements(
            Arrays.asList(
                new MoveRequest("P1", EnumMovement.SCISSORS.getName()),
                new MoveRequest("P2", EnumMovement.LIZARD.getName())
            )
        );
        // Action
        JokenpoResponse response = this.jokenpoService.play();
        // Assertments check
        Assert.assertNotNull(response.getGameResult());
        String expected = "P1 IS THE WINNER!".toUpperCase().trim();
        String notExpected = "NOBODY WON!".toUpperCase().trim();
        Assert.assertNotEquals(notExpected, response.getGameResult());
        Assert.assertEquals(expected, response.getGameResult());
    }

    @Test
    public void invalidMovementPlayingWithExpectException() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2"));

        // Error Expected
        thrown.expect(JokenpoException.class);
        thrown.expectMessage("ERR-2001 - Movement not found");

        // Movements insert
        insertSomeMovements(
            Arrays.asList(
                new MoveRequest("P1", EnumMovement.SCISSORS.getName()),
                new MoveRequest("P2", "OTHER_MOVEMENT")
            )
        );
    }

    @Test
    public void someMovementsPossibilitiesWithSucess() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2", "P3"));
        // First movements
        movementGroupWithDifferentMovements1();
        this.jokenpoService.play();
        // Second movements
        movementGroupWithDifferentMovements2();
        this.jokenpoService.play();
        // Third movements
        movementGroupWithTwoEqualsMovements();
        this.jokenpoService.play();
        // Fourth movements
        movementGroupWithAllEqualsMovements();
        this.jokenpoService.play();
    }

    @Test
    public void playingRemovingAndIncludingSomeMovements() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2"));
        // Movements insert
        this.moveService.insert(new MoveRequest("P2", EnumMovement.PAPER.getName()));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SCISSORS.getName()));
        // Action
        JokenpoResponse response = this.jokenpoService.play();
        // Assertments check
        Assert.assertEquals("P1 IS THE WINNER!", response.getGameResult());
        // Movements insert
        this.moveService.insert(new MoveRequest("P2", EnumMovement.PAPER.getName()));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.STONE.getName()));
        // Movement remove
        this.moveService.deleteByPlayerName("P2");
        // Assertment check
        Assert.assertEquals(1, this.moveService.getAll().size());
        // Movement insert
        this.moveService.insert(new MoveRequest("P2", EnumMovement.SPOCK.getName()));
        // Action
        response = this.jokenpoService.play();
        // Assertment check
        Assert.assertEquals("P2 IS THE WINNER!", response.getGameResult());
    }

    @Test
    public void playingRemovingAndIncludingSomePlayers1() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2"));
        // Movements insert
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SCISSORS.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.STONE.getName()));
        // Player remove
        this.playerService.deleteByName("P1");
        // Expected exception
        thrown.expect(JokenpoException.class);
        // Action
        JokenpoResponse response = this.jokenpoService.play();
    }

    @Test
    public void playingRemovingAndIncludingSomePlayers2() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2"));
        // Movements insert
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SCISSORS.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.STONE.getName()));
        // Player remove
        this.playerService.deleteByName("P1");
        // Player include
        this.playerService.insert(new PlayerRequest("P7"));
        // Movement insert
        this.moveService.insert(new MoveRequest("P7", EnumMovement.PAPER.getName()));
        // Action
        JokenpoResponse response = this.jokenpoService.play();
        // Assertment check
        Assert.assertNotEquals(0, response.getHistory());
        Assert.assertEquals("P7 IS THE WINNER!", response.getGameResult());
    }

    @Test
    public void historyAfterPlayedWithSucess() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2", "P3"));
        // Movements insert
        insertSomeMovements(
            Arrays.asList(
                new MoveRequest("P1", EnumMovement.SCISSORS.getName()),
                new MoveRequest("P2", EnumMovement.LIZARD.getName()),
                new MoveRequest("P3", EnumMovement.STONE.getName())
            )
        );
        // Action
        JokenpoResponse response = this.jokenpoService.play();
        // Assertments check
        Assert.assertNotEquals(0, response.getHistory().size());
        Assert.assertEquals(3, response.getHistory().size());
    }

    @Test
    public void historyBeforePlayWithExpectException() throws JokenpoException {
        // Players insert
        insertSomePlayers(Arrays.asList("P1", "P2", "P3", "P4", "P5"));
        // Expect Exception
        thrown.expect(JokenpoException.class);
        // Action
        JokenpoResponse response = this.jokenpoService.play();
    }

    private List<PlayerResponse> insertSomePlayers(List<String> playerNameList) {
        List<PlayerResponse> list = new ArrayList<>();
        playerNameList.stream()
            .forEach(playerName -> {
                try {
                    list.add(this.playerService.insert(new PlayerRequest(playerName)));
                } catch (JokenpoException e){
                    e.printStackTrace();
                }
            }
        );
        return list;
    }

    private List<MoveResponse> insertSomeMovements(List<MoveRequest> movementList) throws JokenpoException {
        List<MoveResponse> list = new ArrayList<>();
        for(MoveRequest movement : movementList)
            list.add(this.moveService.insert(movement));
        return list;
    }

    private void clearAllData() {
        this.playerService.clearAll();
        this.moveService.clearAll();
    }

    private void movementGroupWithDifferentMovements1() throws JokenpoException {
        // Movements cleared
        this.moveService.clearAll();
        // Movements insert
        insertSomeMovements(
            Arrays.asList(
                new MoveRequest("P1", EnumMovement.SCISSORS.getName()),
                new MoveRequest("P2", EnumMovement.LIZARD.getName()),
                new MoveRequest("P3", EnumMovement.STONE.getName())
            )
        );
    }

    private void movementGroupWithDifferentMovements2() throws JokenpoException {
        // Movements cleared
        this.moveService.clearAll();
        // Movements insert
        insertSomeMovements(
                Arrays.asList(
                        new MoveRequest("P1", EnumMovement.STONE.getName()),
                        new MoveRequest("P2", EnumMovement.PAPER.getName()),
                        new MoveRequest("P3", EnumMovement.SPOCK.getName())
                )
        );
    }

    private void movementGroupWithAllEqualsMovements() throws JokenpoException {
        // Movements cleared
        this.moveService.clearAll();
        // Movements insert
        insertSomeMovements(
                Arrays.asList(
                        new MoveRequest("P1", EnumMovement.LIZARD.getName()),
                        new MoveRequest("P2", EnumMovement.LIZARD.getName()),
                        new MoveRequest("P3", EnumMovement.LIZARD.getName())
                )
        );
    }

    private void movementGroupWithTwoEqualsMovements() throws JokenpoException {
        // Movements cleared
        this.moveService.clearAll();
        // Movements insert
        insertSomeMovements(
                Arrays.asList(
                        new MoveRequest("P1", EnumMovement.PAPER.getName()),
                        new MoveRequest("P2", EnumMovement.LIZARD.getName()),
                        new MoveRequest("P3", EnumMovement.PAPER.getName())
                )
        );
    }

}
