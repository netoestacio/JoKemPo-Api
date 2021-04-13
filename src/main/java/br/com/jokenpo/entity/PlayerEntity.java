package br.com.jokenpo.entity;

import javax.persistence.Entity;
import java.util.Objects;

/**
 * OBSERVACAO : Poderia colocar um extends para uma classe BaseEntity com ID Generate,
 * mas como nao foi utilizado banco de dados, optei por nao inserir isto.
 *
 * */

@Entity
public class PlayerEntity {

    private String name;

    public PlayerEntity(){
    }

    public PlayerEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerEntity that = (PlayerEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return "PlayerEntity{" +
                "name='" + name + '\'' +
                '}';
    }

}
