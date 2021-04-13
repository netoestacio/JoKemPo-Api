package br.com.jokenpo.repository;

import br.com.jokenpo.entity.PlayerEntity;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.enumeration.EnumException;
import br.com.jokenpo.util.PlayerSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OBSERVACAO : Poderia colocar a classe como interface e extends ao JpaRepository.
 * Como o uso de banco de dados nao era obrigatorio, optei por utilizar o padrao Singleton.
 * Contudo, pela decisao de utilizar o Singleton e List, tive que adaptar os
 * repositories (save, delete, findAll, entre outros), conforme abaixo.
 *
 * */


@Repository
@NoRepositoryBean
public class PlayerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerRepository.class);

    public PlayerEntity save(PlayerEntity entity) throws JokenpoException {
        if(PlayerSingleton.getInstance() != null
                && PlayerSingleton.getInstance().add(entity))
            return entity;
        LOGGER.error("Error saving");
        throw new JokenpoException(EnumException.PLAYER_SAVE_ERROR);
    }

    public boolean delete(PlayerEntity entity) throws JokenpoException {
        if(PlayerSingleton.getInstance() == null) {
            LOGGER.error("Error deleting");
            throw new JokenpoException(EnumException.PLAYER_DELETE_ERROR);
        }
        return PlayerSingleton.getInstance().remove(entity);
    }

    public List<PlayerEntity> findAll() throws JokenpoException {
        if(PlayerSingleton.getInstance() == null) {
            LOGGER.error("Error finding all players");
            throw new JokenpoException(EnumException.PLAYER_FIND_ALL_ERROR);
        }
        return PlayerSingleton.getInstance();
    }

    public PlayerEntity findByName(String name) throws JokenpoException {
        List<PlayerEntity> list = findAll().stream()
                .filter(elem -> (elem.getName().compareToIgnoreCase(name) == 0))
                .collect(Collectors.toList());
        Optional<PlayerEntity> opt = list.stream().findFirst();
        if(opt.isPresent()){
            return opt.get();
        }
        LOGGER.info("Player not found : {}", name);
        throw new JokenpoException(EnumException.PLAYER_NOT_FOUND);
    }

}
