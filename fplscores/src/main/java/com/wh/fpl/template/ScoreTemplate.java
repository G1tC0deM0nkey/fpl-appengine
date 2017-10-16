package com.wh.fpl.template;

import com.wh.fpl.core.Gameweek;
import com.wh.fpl.core.Player;
import com.wh.fpl.core.PlayerKey;
import com.wh.fpl.core.Teamsheet;

/**
 * Created by jkaye on 16/10/17.
 */
public class ScoreTemplate {

    private String name;

    private Teamsheet teamsheet;

    private Gameweek gameweek;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teamsheet getTeamsheet() {
        return teamsheet;
    }

    public void setTeamsheet(Teamsheet teamsheet) {
        this.teamsheet = teamsheet;
    }

    public Gameweek getGameweek() {
        return gameweek;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();

        sb.append("<h1>").append(name).append("</h1>");
        sb.append("<p>");

        sb.append("<h3>Starting XI</h3>");

        int total = 0;
        int totalWithSubs = 0;

        for(PlayerKey pk : teamsheet.getStarters()) {

            Player p = gameweek.getLatestScores().get(pk);
            boolean playing = p != null && p.isPlaying();
            int score = teamsheet.getPlayerScore(pk, gameweek);

            sb.append("<p>");

            if(!playing) {
                sb.append("<i>");
            }

            sb.append(pk.getPosition()).append("\t").append(pk.getTeam()).append("\t");
            sb.append(pk.getName()).append("\t").append("-\t<b>");

            if(pk.equals(teamsheet.getCaptain())) {
                sb.append("(C) ");
            }

            sb.append(score).append("</b>\t-\t");

            if(playing) {
                sb.append("(played)");
            }
            else {
                sb.append("</i>");
            }

            sb.append("</p>\n");

            total +=score;
            totalWithSubs += score;
        }

        sb.append("<br/><h3>Substitutes</h3>");

        for(PlayerKey pk : teamsheet.getSubs()) {
            sb.append("<p><i>");
            sb.append(pk.getPosition()).append("\t").append(pk.getTeam()).append("\t");
            sb.append(pk.getName()).append("\t").append("-\t<b>");

            Player p = gameweek.getLatestScores().get(pk);
            boolean playing = p != null && p.isPlaying();
            int score = teamsheet.getPlayerScore(pk, gameweek);

            sb.append(score).append("</b>\t-\t");

            if(playing) {
                sb.append("(played)");
            }

            sb.append("</i></p>\n");

            totalWithSubs += score;
        }


        sb.append("<br/><p><b>Total Scores : ").append(total).append("</b></p>");
        sb.append("<p><b>Total (subs) : ").append(totalWithSubs).append("</b></p>");

        sb.append("</p>");

        return sb.toString();
    }

}
