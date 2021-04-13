package br.com.jokenpo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class JokenpoResponse {

    @NotNull
    @JsonProperty(value = "result")
    private String gameResult;

    @NotNull
    private List<String> history;

    public JokenpoResponse(String gameResult, List<String> history) {
        this.gameResult = gameResult;
        this.history = history;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokenpoResponse that = (JokenpoResponse) o;
        return Objects.equals(gameResult, that.gameResult) &&
                Objects.equals(history, that.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameResult, history);
    }

    @Override
    public String toString() {
        return "JokenpoResponse{" +
                "gameResult='" + gameResult + '\'' +
                ", history=" + history +
                '}';
    }

}
