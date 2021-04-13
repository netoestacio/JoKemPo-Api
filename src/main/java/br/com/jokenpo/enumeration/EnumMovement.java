package br.com.jokenpo.enumeration;

import br.com.jokenpo.exception.JokenpoException;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public enum EnumMovement {

    SPOCK("SPOCK"),
    SCISSORS("SCISSORS"),
    PAPER("PAPER"),
    STONE("STONE"),
    LIZARD("LIZARD");

    private String name;
    private List<EnumMovement> weakness;

    static {
        SPOCK.setWeakness(asList(LIZARD, PAPER));
        SCISSORS.setWeakness(asList(SPOCK, STONE));
        PAPER.setWeakness(asList(SCISSORS, LIZARD));
        STONE.setWeakness(asList(SPOCK, PAPER));
        LIZARD.setWeakness(asList(SCISSORS, STONE));
    }

    EnumMovement(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EnumMovement> getWeakness() {
        return weakness;
    }

    public void setWeakness(List<EnumMovement> weakness) {
        this.weakness = weakness;
    }

    public static EnumMovement getEnumMovementByName(String name) throws JokenpoException {
        for (EnumMovement elem : Arrays.asList(EnumMovement.values())) {
            if (name.equals(elem.getName())) {
                return elem;
            }
        }
        throw new JokenpoException(EnumException.MOVEMENT_NOT_FOUND);
    }

}
