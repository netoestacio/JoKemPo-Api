package br.com.jokenpo.controller;

import br.com.jokenpo.dto.api.ApiResponse;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.service.impl.JokenpoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/play")
@CrossOrigin(origins = "*")
public class JokenpoController {

    private JokenpoServiceImpl jokenpoService;

    @Autowired
    public JokenpoController(JokenpoServiceImpl jokenpoService) {
        this.jokenpoService = jokenpoService;
    }

    @DeleteMapping(value = "")
    public ResponseEntity<Object> reset() throws JokenpoException {
        return ResponseEntity.ok(new ApiResponse<>(this.jokenpoService.clear()));
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> play() throws JokenpoException {
        return ResponseEntity.ok(new ApiResponse<>(this.jokenpoService.play()));
    }

}
