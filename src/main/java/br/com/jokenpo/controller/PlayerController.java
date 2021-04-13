package br.com.jokenpo.controller;

import br.com.jokenpo.dto.PlayerRequest;
import br.com.jokenpo.dto.api.ApiResponse;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.service.impl.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = "*")
public class PlayerController {

    private PlayerServiceImpl playerService;

    @Autowired
    public PlayerController(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> insert(@Valid @RequestBody PlayerRequest playerRequest)
            throws JokenpoException {
        return ResponseEntity.ok(
                new ApiResponse<>(this.playerService.insert(playerRequest)));
    }

    @DeleteMapping(value = "")
    public ResponseEntity<Object> delete(@PathParam("playerName") String playerName) throws JokenpoException {
        return ResponseEntity.ok(new ApiResponse<>(this.playerService.deleteByName(playerName)));
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAll() throws JokenpoException {
        return ResponseEntity.ok(new ApiResponse<>(this.playerService.getAll()));
    }

}
