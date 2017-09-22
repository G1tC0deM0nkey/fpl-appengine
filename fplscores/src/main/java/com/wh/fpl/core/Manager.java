package com.wh.fpl.core;

import java.util.List;
import java.util.Map;

/**
 * Created by jkaye on 18/09/17.
 */
public class Manager {

    private String name;

    private List<PlayerKey> players;

    private Map<Integer, Teamsheet> teamsheets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlayerKey> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerKey> players) {
        this.players = players;
    }

    public Map<Integer, Teamsheet> getTeamsheets() {
        return teamsheets;
    }

    public void setTeamsheets(Map<Integer, Teamsheet> teamsheets) {
        this.teamsheets = teamsheets;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "name='" + name + '\'' +
                ", players=" + players +
                ", teamsheets=" + teamsheets +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Manager manager = (Manager) o;

        if (name != null ? !name.equals(manager.name) : manager.name != null) return false;
        if (players != null ? !players.equals(manager.players) : manager.players != null) return false;
        return teamsheets != null ? teamsheets.equals(manager.teamsheets) : manager.teamsheets == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        result = 31 * result + (teamsheets != null ? teamsheets.hashCode() : 0);
        return result;
    }
}
