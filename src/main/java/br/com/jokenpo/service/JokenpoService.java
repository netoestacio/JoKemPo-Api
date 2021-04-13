package br.com.jokenpo.service;

import br.com.jokenpo.dto.JokenpoResponse;
import br.com.jokenpo.dto.PlayerResponse;
import br.com.jokenpo.exception.JokenpoException;

import java.util.List;

public interface JokenpoService {

    List<PlayerResponse> clear() throws JokenpoException;

    JokenpoResponse play() throws JokenpoException;

}
