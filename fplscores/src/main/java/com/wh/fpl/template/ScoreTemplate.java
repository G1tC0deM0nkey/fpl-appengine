package com.wh.fpl.template;

import com.wh.fpl.core.*;

/**
 * Created by jkaye on 16/10/17.
 */
public class ScoreTemplate {

    private String name;

    private TeamScore teamScore;

    private Gameweek gameweek;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamScore getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(TeamScore teamsheet) {
        this.teamScore = teamsheet;
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

        sb.append("<h3>Starting XI with Substitutions</h3>");

        for(PlayerKey pk : teamScore.getPlayers()) {

            Player p = gameweek.getLatestScores().get(pk);
            boolean playing = p != null && p.isPlaying();
            int score = teamScore.getScore(pk);

            sb.append("<p>");

            if(!playing) {
                sb.append("<i>");
            }

            sb.append(pk.getPosition()).append("\t").append(pk.getTeam()).append("\t");
            sb.append(pk.getName()).append("\t").append("-\t<b>");

            if(pk.equals(teamScore.getCaptain())) {
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

        }

        sb.append("<br/><h3>Substitutes</h3>");

        for(String s : teamScore.getSubstitutions()) {
            sb.append("<p><i>");
            sb.append(s);
            sb.append("</i></p>\n");
        }

        sb.append("<br/><p><b>Total Scores : ").append(teamScore.getTotalScore()).append("</b></p>");

        sb.append("</p>");

        return sb.toString();
    }

}
