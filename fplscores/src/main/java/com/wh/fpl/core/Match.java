package com.wh.fpl.core;

/**
 * Created by johnk on 27/09/2017.
 */
public class Match {

    private Fixture fixture;

    private Gameweek gameweek;

    private Teamsheet homeTeamsheet;

    private Teamsheet awayTeamsheet;

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public Teamsheet getAwayTeamsheet() {
        return awayTeamsheet;
    }

    public void setAwayTeamsheet(Teamsheet awayTeamsheet) {
        this.awayTeamsheet = awayTeamsheet;
    }

    public Teamsheet getHomeTeamsheet() {
        return homeTeamsheet;
    }

    public void setHomeTeamsheet(Teamsheet homeTeamsheet) {
        this.homeTeamsheet = homeTeamsheet;
    }

    public Gameweek getGameweek() {
        return gameweek;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }
}
