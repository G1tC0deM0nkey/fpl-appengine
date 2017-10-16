package com.wh.fpl.control;

import com.wh.fpl.core.*;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by jkaye on 18/09/17.
 */
public class OpenGameweek {

    private int gameMonth;

    private int gameweek;

    public OpenGameweek(int gameMonth, int gameweek) {
        this.gameMonth = gameMonth;
        this.gameweek = gameweek;
    }

    public Gameweek open() throws Exception {

        Gameweek gw = new Gameweek(gameMonth, gameweek);

        PlayerParser playerParser = new PlayerParser("https://fantasy.premierleague.com/player-list/");
        List<Player> players = playerParser.getPlayers();

        for (Player p : players) {
            gw.getStartingScores().put(p.getPlayerKey(), p);
        }

        gw.export(new PrintWriter(System.out));

        return gw;

    }

    public static void main(String [] args) throws Exception {

        OpenGameweek openGameweek = new OpenGameweek(GameweekConstants.MONTH, GameweekConstants.WEEK);
        Gameweek current = openGameweek.open();

        FSContext context = new FSContext("data");
        context.storeGameweek(current);

        List <Fixture> fixtures = context.loadFixtures(current);
        context.storeFixtures(current, fixtures);

        Fixtures fixturesObj = new Fixtures(context.loadFixtures());
        Gameweek last = context.loadGameweek(fixturesObj.nextGamemonth(openGameweek.gameweek),
                fixturesObj.nextGameweek(openGameweek.gameweek));

        Map<String, Teamsheet> lastTeamsheets = context.loadTeamsheets(last);
        Map <String, Teamsheet> currentTeamsheets = context.loadTeamsheets(current);

        if(currentTeamsheets.size() == 0) {
            currentTeamsheets = lastTeamsheets;
            for(String manager : currentTeamsheets.keySet()) {
                //Store last month team sheets
                Teamsheet t = currentTeamsheets.get(manager);
                context.storeTeamsheet(t, manager, current.getGameMonth(), current.getGameWeek());

                //Also store these to the game month directory
                if(current.getGameMonth() != last.getGameMonth()) {
                    context.storeTeamsheet(t, manager, current.getGameMonth());
                }
            }
        }

        System.exit(0);

    }
}
