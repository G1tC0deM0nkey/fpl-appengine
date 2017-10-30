package com.wh.fpl.template;

import com.wh.fpl.core.Fixture;
import com.wh.fpl.core.Fixtures;
import com.wh.fpl.core.Match;
import com.wh.fpl.utils.SimpleNamer;

import java.util.List;

public class MatchTemplate {

    private int gameMonth;

    private int gameWeek;

    private boolean active;

    private Fixtures fixtures;

    private List<Match> matches;

    private String updateStatus;

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public void setGameMonth(int gameMonth) {
        this.gameMonth = gameMonth;
    }

    public void setGameWeek(int gameWeek) {
        this.gameWeek = gameWeek;
    }

    public void active(boolean active) {
        this.active = active;
    }

    public void setFixtures(Fixtures fixtures) {
        this.fixtures = fixtures;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public String render() {

        StringBuilder sb = new StringBuilder();

        sb.append("<html><head><title>Gameweek ");
        sb.append(gameWeek).append(" Month ").append(gameMonth);
        sb.append("</title></head><body>");
        sb.append("<h2>Gamemonth ").append(gameMonth).append(" Gameweek ").append(gameWeek).append("</h2>");

        if(active) {
            sb.append("<p><b>(Active)</b></p>");
        }
        else {
            sb.append("<p><i>(Inactive)</i></p>");
        }

        if(updateStatus != null) {
            sb.append("<p>").append(updateStatus).append("</p>");
        }
        sb.append("<br/>");

        for(Match m : matches) {

            sb.append("<p>");

            sb.append(" <a href=\"squad?name=").append(SimpleNamer.simpleName(m.getFixture().getHome())).append("\">");
            sb.append(m.getFixture().getHome()).append("</a> ");

            if(m.getHomeTeamScore() != null) {
                sb.append(" <a href=\"score?name=").append(SimpleNamer.simpleName(m.getFixture().getHome())).append("&month=").append(gameMonth);
                sb.append("&week=").append(gameWeek).append("\">");
                sb.append(m.getHomeTeamScore().getTotalScore()).append("</a> ");
            }

            sb.append(" vs ");
            if(m.getAwayTeamScore() != null) {
                sb.append(" <a href=\"score?name=").append(SimpleNamer.simpleName(m.getFixture().getAway())).append("&month=").append(gameMonth);
                sb.append("&week=").append(gameWeek).append("\">");
                sb.append(m.getAwayTeamScore().getTotalScore()).append("</a> ");
            }

            sb.append(" <a href=\"squad?name=").append(SimpleNamer.simpleName(m.getFixture().getAway())).append("\">");
            sb.append(m.getFixture().getAway()).append("</a> ");

            sb.append("</p>");
        }

        sb.append("<br/>");
        sb.append("<a href=\"matches?month=").append(fixtures.prevGamemonth(gameWeek)).append("&week=").append(fixtures.prevGameweek(gameWeek));
        sb.append("\"> &lt;&lt;&lt; Previous </a> ");
        sb.append("<a href=\"matches?month=").append(fixtures.nextGamemonth(gameWeek)).append("&week=").append(fixtures.nextGameweek(gameWeek));
        sb.append("\"> Next &gt;&gt;&gt; </a>");

        return sb.toString();
    }


}
