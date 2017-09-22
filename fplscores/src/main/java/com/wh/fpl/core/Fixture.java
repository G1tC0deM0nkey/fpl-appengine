package com.wh.fpl.core;

/**
 * Created by jkaye on 20/09/17.
 */
public class Fixture {

    int gameMonth;

    int gameWeek;

    String home;

    String away;

    public int getGameMonth() {
        return gameMonth;
    }

    public void setGameMonth(int gameMonth) {
        this.gameMonth = gameMonth;
    }

    public int getGameWeek() {
        return gameWeek;
    }

    public void setGameWeek(int gameWeek) {
        this.gameWeek = gameWeek;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    @Override
    public String toString() {
        return "Fixture{" +
                "gameMonth=" + gameMonth +
                ", gameWeek=" + gameWeek +
                ", home='" + home + '\'' +
                ", away='" + away + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fixture fixture = (Fixture) o;

        if (gameMonth != fixture.gameMonth) return false;
        if (gameWeek != fixture.gameWeek) return false;
        if (home != null ? !home.equals(fixture.home) : fixture.home != null) return false;
        return away != null ? away.equals(fixture.away) : fixture.away == null;
    }

    @Override
    public int hashCode() {
        int result = gameMonth;
        result = 31 * result + gameWeek;
        result = 31 * result + (home != null ? home.hashCode() : 0);
        result = 31 * result + (away != null ? away.hashCode() : 0);
        return result;
    }
}
