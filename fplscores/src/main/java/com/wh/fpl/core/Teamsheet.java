package com.wh.fpl.core;

import java.util.List;

/**
 * Created by jkaye on 18/09/17.
 */
public class Teamsheet {

    private int gamemonth;

    private List <PlayerKey> starters;

    private List<PlayerKey> subs;

    private PlayerKey captain;

    private PlayerKey vice;

    public int getGamemonth() {
        return gamemonth;
    }

    public void setGamemonth(int gamemonth) {
        this.gamemonth = gamemonth;
    }

    public List<PlayerKey> getStarters() {
        return starters;
    }

    public void setStarters(List<PlayerKey> starters) {
        this.starters = starters;
    }

    public List<PlayerKey> getSubs() {
        return subs;
    }

    public void setSubs(List<PlayerKey> subs) {
        this.subs = subs;
    }

    public PlayerKey getCaptain() {
        return captain;
    }

    public void setCaptain(PlayerKey captain) {
        this.captain = captain;
    }

    public PlayerKey getVice() {
        return vice;
    }

    public void setVice(PlayerKey vice) {
        this.vice = vice;
    }

    @Override
    public String toString() {
        return "Teamsheet{" +
                "gamemonth=" + gamemonth +
                ", starters=" + starters +
                ", subs=" + subs +
                ", captain=" + captain +
                ", vice=" + vice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teamsheet teamsheet = (Teamsheet) o;

        if (gamemonth != teamsheet.gamemonth) return false;
        if (starters != null ? !starters.equals(teamsheet.starters) : teamsheet.starters != null) return false;
        if (subs != null ? !subs.equals(teamsheet.subs) : teamsheet.subs != null) return false;
        if (captain != null ? !captain.equals(teamsheet.captain) : teamsheet.captain != null) return false;
        return vice != null ? vice.equals(teamsheet.vice) : teamsheet.vice == null;
    }

    @Override
    public int hashCode() {
        int result = gamemonth;
        result = 31 * result + (starters != null ? starters.hashCode() : 0);
        result = 31 * result + (subs != null ? subs.hashCode() : 0);
        result = 31 * result + (captain != null ? captain.hashCode() : 0);
        result = 31 * result + (vice != null ? vice.hashCode() : 0);
        return result;
    }
}
