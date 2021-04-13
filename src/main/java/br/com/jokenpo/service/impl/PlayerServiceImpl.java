package br.com.jokenpo.service.impl;

import br.com.jokenpo.dto.PlayerRequest;
import br.com.jokenpo.dto.PlayerResponse;
import br.com.jokenpo.entity.PlayerEntity;
import br.com.jokenpo.entity.mapper.PlayerMapper;
import br.com.jokenpo.enumeration.EnumException;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.repository.PlayerRepository;
import br.com.jokenpo.service.PlayerService;
import br.com.jokenpo.util.PlayerSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private PlayerRepository playerRepository;
    private MoveServiceImpl moveService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, MoveServiceImpl moveService){
        this.playerRepository = playerRepository;
        this.moveService = moveService;
    }

    public PlayerResponse insert(PlayerRequest player) throws JokenpoException {
        if(this.verifyIfAlreadyExistsByName(player.getName())){
            LOGGER.error("Player already exists");
            throw new JokenpoException(EnumException.PLAYER_ALREADY_EXISTS);
        }
        LOGGER.debug("Insert new player - Request: " + player.toString());
        PlayerEntity entity = PlayerMapper.requestToEntity(player);
        LOGGER.debug("Inserting player");
        entity = this.playerRepository.save(entity);
        LOGGER.debug("Creating response object");
        return PlayerMapper.entityToResponse(entity);
    }

    public List<PlayerResponse> getAll() throws JokenpoException {
        LOGGER.debug("Finding all players");
        List<PlayerEntity> entityList = this.playerRepository.findAll();
        List<PlayerResponse> response = new ArrayList<>();
        entityList
                .forEach(elem -> {
                    response.add(PlayerMapper.entityToResponse(elem));
                });
        LOGGER.debug("Players filtered");
        return response;
    }

    public PlayerEntity getEntityByName(String name) throws JokenpoException {
        LOGGER.debug("Finding player by name : {}", name);
        return this.playerRepository.findByName(name);
    }

    public List<PlayerResponse> deleteByName(String name) throws JokenpoException {
        if(StringUtils.isEmpty(name)){
            LOGGER.error("Param name invalid");
            throw new JokenpoException(EnumException.INVALID_PARAM);
        }
        try {
            this.moveService.deleteByPlayerName(name);
        } catch (JokenpoException ex){
            LOGGER.debug("Player without movement already");
        }
        LOGGER.debug("Finding player by name : {}", name);
        PlayerEntity entity = this.playerRepository.findByName(name);
        LOGGER.debug("Removing player");
        if(this.playerRepository.delete(entity)){
            return this.getAll();
        }
        LOGGER.error("Error deleting player");
        throw new JokenpoException(EnumException.PLAYER_DELETE_ERROR);
    }

    public void clearAll(){
        PlayerSingleton.clear();
    }

    private Boolean verifyIfAlreadyExistsByName(String name) {
        try {
            if (!Objects.isNull(this.playerRepository.findByName(name))) {
                return true;
            }
        } catch (JokenpoException e) {
            return false;
        }
        return false;
    }

}
