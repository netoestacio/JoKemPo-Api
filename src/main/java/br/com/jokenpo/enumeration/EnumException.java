package br.com.jokenpo.enumeration;

import java.util.Arrays;


public enum EnumException {

    GENERIC_ERROR("ERR-0001", "JOKENPO", "GENERIC ERROR", "GENERIC ERROR", "Generic error"),
    INVALID_PARAM("ERR-0002", "JOKENPO", "PARAM", "INVALID", "Invalid parameter"),

    // PLAYER
    PLAYER_NOT_FOUND("ERR-1001", "JOKENPO", "PLAYER", "NOT FOUND", "Player not found"),
    PLAYER_ALREADY_EXISTS("ERR-1002", "JOKENPO", "PLAYER", "ALREADY EXISTS", "Player already registered"),
    PLAYER_INVALID_NAME("ERR-1003", "JOKENPO", "PLAYER", "NAME", "Invalid player name"),
    PLAYER_SAVE_ERROR("ERR-1004", "JOKENPO", "PLAYER", "SAVE", "Error saving the player"),
    PLAYER_DELETE_ERROR("ERR-1005", "JOKENPO", "PLAYER", "SAVE", "Error deleting the player"),
    PLAYER_FIND_ALL_ERROR("ERR-1006", "JOKENPO", "PLAYER", "FIND ALL", "Error at looking for the players"),

    // MOVEMENT
    MOVEMENT_NOT_FOUND("ERR-2001", "JOKENPO", "MOVEMENT", "NOT FOUND", "Movement not found"),
    MOVEMENT_ALREADY_EXISTS("ERR-2002", "JOKENPO", "MOVEMENT", "ALREADY EXISTS", "This player has played before"),
    MOVEMENT_INVALID("ERR-2003", "JOKENPO", "MOVEMENT", "INVALID", "Invalid movement"),
    MOVEMENT_SAVE_ERROR("ERR-2004", "JOKENPO", "MOVEMENT", "SAVE", "Error saving"),
    MOVEMENT_DELETE_ERROR("ERR-2005", "JOKENPO", "MOVEMENT", "SAVE", "Error deleting"),
    MOVEMENT_FIND_ALL_ERROR("ERR-2006", "JOKENPO", "MOVEMENT", "FIND ALL", "Error locating movements"),

    // JOCKENPO - PLAY
    NOBODY_PLAYING("ERR-3001", "JOKENPO", "PLAY", "NOBODY", "There's no one playing"),
    INSUFFICIENT_PLAYERS("ERR-3002", "JOKENPO", "PLAY", "INSUFFICIENT PLAYERS", "Insufficient number of players"),
    INSUFFICIENT_MOVEMENTS("ERR-3002", "JOKENPO", "PLAY", "INSUFFICIENT MOVEMENTS", "Number of movements still insufficient"),
    PLAYERS_PENDING("ERR-3003", "JOKENPO", "PLAY", "PLAYERS PENDING", "There are players who have not yet chosen");

    private String code;
    private String origin;
    private String type;
    private String subType;
    private String message;

    EnumException(String code, String origin, String type, String subType, String message) {
        this.code = code;
        this.origin = origin;
        this.type = type;
        this.subType = subType;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getOrigin() {
        return origin;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static EnumException getEnumExceptionByCode(String code){
        for (EnumException elem : Arrays.asList(EnumException.values())) {
            if (code.equals(elem.getCode())) {
                return elem;
            }
        }
        return EnumException.GENERIC_ERROR;
    }

}
