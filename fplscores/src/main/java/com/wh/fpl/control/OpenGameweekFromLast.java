package com.wh.fpl.control;

import com.wh.fpl.core.*;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by jkaye on 18/09/17.
 */
public class OpenGameweekFromLast {

    private int gameMonth;

    private int gameweek;

    private Gameweek last;

    public OpenGameweekFromLast(int gameMonth, int gameweek) {
        this.gameMonth = gameMonth;
        this.gameweek = gameweek;
    }

    public Gameweek getLast() {
        return last;
    }

    public void setLast(Gameweek last) {
        this.last = last;
    }

    public Gameweek open() throws Exception {

        Gameweek gw = new Gameweek(gameMonth, gameweek);

        if(last != null) {
            for (PlayerKey pk : last.getLatestScores().keySet()) {
                gw.getStartingScores().put(pk, last.getLatestScores().get(pk));
            }
        }

        gw.export(new PrintWriter(System.out));

        return gw;

    }

    public static void main(String [] args) throws Exception {

        OpenGameweekFromLast openGameweek = new OpenGameweekFromLast(GameweekConstants.MONTH, GameweekConstants.WEEK);

        FSContext context = new FSContext("data");

        Fixtures fixtures = new Fixtures(context.loadFixtures());
        Gameweek last = context.loadGameweek(fixtures.prevGamemonth(openGameweek.gameweek),
                fixtures.prevGameweek(openGameweek.gameweek));
        openGameweek.setLast(last);

        Gameweek current = openGameweek.open();
        context.storeGameweek(current);

        Map <String, Teamsheet> lastTeamsheets = context.loadTeamsheets(last);
        Map <String, Teamsheet> currentTeamsheets = context.loadTeamsheets(current);

        if(currentTeamsheets.size() == 0) {
            currentTeamsheets = lastTeamsheets;
            for(String manager : currentTeamsheets.keySet()) {
                //Store last month team sheets
                Teamsheet t = currentTeamsheets.get(manager);
                currentTeamsheets.put(manager, t);
                context.storeTeamsheet(t, manager, current.getGameMonth(), current.getGameWeek());

                //Also store these to the game month directory
                if(current.getGameMonth() != last.getGameMonth()) {
                    context.storeTeamsheet(t, manager, current.getGameMonth());
                }
            }
        }

        for(String m : currentTeamsheets.keySet()) {
            context.storeTeamsheet(currentTeamsheets.get(m), m, current.getGameMonth(), current.getGameWeek());
        }

        System.exit(0);

    }
}
