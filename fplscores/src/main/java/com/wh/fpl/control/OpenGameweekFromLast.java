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

    private FSContext fsContext;

    public OpenGameweekFromLast(int gameMonth, int gameweek) {
        this.gameMonth = gameMonth;
        this.gameweek = gameweek;
    }

    public void setFilesystemContext(FSContext context) {
        this.fsContext = context;
    }

    public Gameweek open() throws Exception {

        Gameweek current = new Gameweek(gameMonth, gameweek);

        Fixtures fixtures = new Fixtures(fsContext.loadFixtures());
        Gameweek last = fsContext.loadGameweek(fixtures.prevGamemonth(current.getGameWeek()),
                fixtures.prevGameweek(current.getGameWeek()));

        if(last != null) {
            for (PlayerKey pk : last.getLatestScores().keySet()) {
                current.getStartingScores().put(pk, last.getLatestScores().get(pk));
            }
        }

        System.out.println("Storing " + current.getGameWeek() + " " + current.getGameMonth());
        fsContext.storeGameweek(current);

        List <Fixture> gameweekFixtures = fsContext.loadFixtures(current);
        fsContext.storeFixtures(current, gameweekFixtures);

        Map <String, Teamsheet> lastTeamsheets = fsContext.loadTeamsheets(last);
        Map <String, Teamsheet> currentTeamsheets = fsContext.loadTeamsheets(current);

        if(currentTeamsheets.size() == 0) {
            currentTeamsheets = lastTeamsheets;
            for(String manager : currentTeamsheets.keySet()) {
                //Store last month team sheets
                Teamsheet t = currentTeamsheets.get(manager);
                currentTeamsheets.put(manager, t);
                fsContext.storeTeamsheet(t, manager, current.getGameMonth(), current.getGameWeek());

                //Also store these to the game month directory
                if(current.getGameMonth() != last.getGameMonth()) {
                    fsContext.storeTeamsheet(t, manager, current.getGameMonth());
                }
            }
        }

        for(String m : currentTeamsheets.keySet()) {
            fsContext.storeTeamsheet(currentTeamsheets.get(m), m, current.getGameMonth(), current.getGameWeek());
        }

        return current;

    }

    public static void main(String [] args) throws Exception {

        OpenGameweekFromLast openGameweek = new OpenGameweekFromLast(GameweekConstants.MONTH, GameweekConstants.WEEK);

        FSContext context = new FSContext("data");
        openGameweek.setFilesystemContext(context);

        Gameweek current = openGameweek.open();
        context.storeGameweek(current);

        System.exit(0);

    }
}
