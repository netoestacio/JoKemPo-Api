package br.com.jokenpo.entity.mapper;

import br.com.jokenpo.dto.PlayerRequest;
import br.com.jokenpo.dto.PlayerResponse;
import br.com.jokenpo.entity.PlayerEntity;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerMapper.class);

    private static ModelMapper MAPPER = new ModelMapper();

    public static PlayerEntity requestToEntity(PlayerRequest playerRequest){
        LOGGER.debug("Converting: request object to entity object");
        return MAPPER.map(playerRequest, PlayerEntity.class);
    }

    public static PlayerResponse entityToResponse(PlayerEntity entity) {
        LOGGER.debug("Converting: entity object to response object");
        return MAPPER.map(entity, PlayerResponse.class);
    }

}
