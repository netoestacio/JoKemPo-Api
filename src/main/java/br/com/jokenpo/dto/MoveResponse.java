package br.com.jokenpo.dto;

import br.com.jokenpo.enumeration.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class MoveResponse {

    @NotNull(message = "Player is required")
    private PlayerResponse player;

    @NotNull(message = "Movement is required")
    private EnumMovement movement;

    public MoveResponse(){
    }

    public MoveResponse(PlayerResponse player, EnumMovement movement) {
        this.player = player;
        this.movement = movement;
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerResponse player) {
        this.player = player;
    }

    public EnumMovement getMovement() {
        return movement;
    }

    public void setMovement(EnumMovement movement) {
        this.movement = movement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveResponse that = (MoveResponse) o;
        return Objects.equals(player, that.player) &&
                movement == that.movement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, movement);
    }

    @Override
    public String toString() {
        return "MoveResponse{" +
                "player=" + player +
                ", movement=" + movement +
                '}';
    }

}
