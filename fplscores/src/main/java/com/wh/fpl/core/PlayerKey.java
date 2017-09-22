package com.wh.fpl.core;

/**
 * Created by jkaye on 18/09/17.
 */
public class PlayerKey {

    private String name;

    private String team;

    private String position;

    public PlayerKey(String name, String team, String position) {
        this.name = name;
        this.team = team;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public String getPosition() {
        return position;
    }


    @Override
    public String toString() {
        return "PlayerKey{" +
                "name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerKey playerKey = (PlayerKey) o;

        if (name != null ? !name.equals(playerKey.name) : playerKey.name != null) return false;
        if (team != null ? !team.equals(playerKey.team) : playerKey.team != null) return false;
        return position != null ? position.equals(playerKey.position) : playerKey.position == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }
}
