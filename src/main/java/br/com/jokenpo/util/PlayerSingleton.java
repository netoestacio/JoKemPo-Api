package br.com.jokenpo.util;

import br.com.jokenpo.entity.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public final class PlayerSingleton {

    private static List<PlayerEntity> PLAYER_INSTANCE;
    private static String INFO = "Player Singleton Instance";

    private PlayerSingleton(){
    }

    public static List<PlayerEntity> getInstance() {
        if(PLAYER_INSTANCE == null) {
            PLAYER_INSTANCE = new ArrayList<PlayerEntity>();
        }
        return PLAYER_INSTANCE;
    }

    public static List<PlayerEntity> clear(){
        PLAYER_INSTANCE = new ArrayList<PlayerEntity>();
        return getInstance();
    }

    public String getInfo() {
        return this.INFO;
    }

}
