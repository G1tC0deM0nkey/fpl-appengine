package com.wh.fpl.core;

/**
 * Created by jkaye on 29/08/17.
 */
public class Player {

    private String position;

    private String name;

    private String team;

    private String score;

    private String value;

    private boolean played;

    public Player() {
    }

    public Player(PlayerKey pk) {
        this.name = pk.getName();
        this.position = pk.getPosition();
        this.team = pk.getTeam();
    }

    @Override
    public String toString() {
        return "Player{" +
                "position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", score='" + score + '\'' +
                ", value='" + value + '\'' +
                ", played=" + played +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (played != player.played) return false;
        if (position != null ? !position.equals(player.position) : player.position != null) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (team != null ? !team.equals(player.team) : player.team != null) return false;
        if (score != null ? !score.equals(player.score) : player.score != null) return false;
        return value != null ? value.equals(player.value) : player.value == null;
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (played ? 1 : 0);
        return result;
    }

    public boolean isPlaying() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PlayerKey getPlayerKey() {
        return new PlayerKey(name, team, position);
    }

    public void update(Player p) {

        //Update the played status if the score is non null
        if(!this.score.equals(p.getScore())) {
            played = true;
        }

        this.score = p.getScore();
    }
}
