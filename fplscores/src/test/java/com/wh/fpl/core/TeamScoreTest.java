package com.wh.fpl.core;

import org.junit.Test;

/**
 * Created by johnk on 27/10/2017.
 */
public class TeamScoreTest {

    @Test
    public void testTeamScore3_8() throws Exception {

        FSContext ctx = new FSContext("data");

        Gameweek gameweek = ctx.loadGameweek(3, 8);
        Teamsheet teamsheet = ctx.loadTeamsheet("johnk", gameweek);

        TeamScore score = new TeamScore(teamsheet);
        score.score(gameweek, TeamScore.Style.SUBSTITUTIONS);

        for(PlayerKey p : score.getPlayers()) {
            System.out.println(p.getName() + " === " + score.getScore(p));
        }

        System.out.println("----------------------");
        System.out.println(score.getTotalScore());
        System.out.println("----------------------");

        for(String s : score.getSubstitutions()) {
            System.out.println(s);
        }

    }


    @Test
    public void testTeamScore3_9() throws Exception {

        FSContext ctx = new FSContext("data");

        Gameweek gameweek = ctx.loadGameweek(3, 9);
        Teamsheet teamsheet = ctx.loadTeamsheet("johnk", gameweek);

        TeamScore score = new TeamScore(teamsheet);
        score.score(gameweek, TeamScore.Style.SUBSTITUTIONS);

        for(PlayerKey p : score.getPlayers()) {
            System.out.println(p.getName() + " === " + score.getScore(p));
        }

        System.out.println("----------------------");
        System.out.println(score.getTotalScore());
        System.out.println("----------------------");

        for(String s : score.getSubstitutions()) {
            System.out.println(s);
        }

    }

}
