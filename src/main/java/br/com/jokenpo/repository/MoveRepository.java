package br.com.jokenpo.repository;

import br.com.jokenpo.entity.MoveEntity;
import br.com.jokenpo.exception.JokenpoException;
import br.com.jokenpo.util.MoveSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import br.com.jokenpo.enumeration.EnumException;

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
public class MoveRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveRepository.class);

    public MoveEntity save(MoveEntity entity) throws JokenpoException {
        if(MoveSingleton.getInstance() != null
                && MoveSingleton.getInstance().add(entity))
            return entity;
        LOGGER.error("Error saving");
        throw new JokenpoException(EnumException.MOVEMENT_SAVE_ERROR);
    }

    public boolean delete(MoveEntity entity) throws JokenpoException {
        if(MoveSingleton.getInstance() == null) {
            LOGGER.error("Error deleting");
            throw new JokenpoException(EnumException.MOVEMENT_DELETE_ERROR);
        }
        return MoveSingleton.getInstance().remove(entity);
    }

    public List<MoveEntity> findAll() throws JokenpoException {
        if(MoveSingleton.getInstance() == null) {
            LOGGER.error("Error finding all movements");
            throw new JokenpoException(EnumException.MOVEMENT_FIND_ALL_ERROR);
        }
        return MoveSingleton.getInstance();
    }

    public MoveEntity findByPlayerName(String playerName) throws JokenpoException {
        List<MoveEntity> list = findAll().stream()
                .filter(elem -> (elem.getPlayer().getName().compareToIgnoreCase(playerName) == 0))
                .collect(Collectors.toList());
        Optional<MoveEntity> opt = list.stream().findFirst();
        if(opt.isPresent()){
            return opt.get();
        }
        LOGGER.error("Player movement not found : {}", playerName);
        throw new JokenpoException(EnumException.MOVEMENT_NOT_FOUND);
    }

}
