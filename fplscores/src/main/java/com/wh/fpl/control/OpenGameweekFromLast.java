package com.wh.fpl.control;

import com.wh.fpl.core.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by jkaye on 18/09/17.
 */
public class OpenGameweekFromLast {

    private static final Logger LOG = LogManager.getLogger(OpenGameweekFromLast.class);

    private int gameMonth;

    private int gameweek;

    private FSContext fsContext;

    public OpenGameweekFromLast(int gameMonth, int gameweek) {
        this.gameMonth = gameMonth;
        this.gameweek = gameweek;
    }

    public void setFilesystemContext(FSContext context) {
        this.fsContext = context;
    }

    public Gameweek open() throws Exception {

        Gameweek newCurrent = new Gameweek(gameMonth, gameweek);

        Fixtures fixtures = new Fixtures(fsContext.loadFixtures());
        Gameweek oldCurrent = fsContext.loadGameweek(fixtures.prevGamemonth(newCurrent.getGameWeek()),
                fixtures.prevGameweek(newCurrent.getGameWeek()));

        LOG.info("Opening new gameweek (" + gameMonth + " / " + gameMonth + ") from last gameweek ("
                + oldCurrent.getGameMonth() + " / " + oldCurrent.getGameWeek() + ")");

        if(oldCurrent != null) {
            for (PlayerKey pk : oldCurrent.getLatestScores().keySet()) {
                newCurrent.getStartingScores().put(pk, oldCurrent.getLatestScores().get(pk));
            }
        }

        System.out.println("Storing " + newCurrent.getGameWeek() + " " + newCurrent.getGameMonth() + " players list");
        fsContext.storeGameweek(newCurrent);

        List <Fixture> gameweekFixtures = fsContext.loadFixtures(newCurrent);
        fsContext.storeFixtures(newCurrent, gameweekFixtures);

        Map <String, Teamsheet> lastTeamsheets = fsContext.loadTeamsheets(oldCurrent);
        Map <String, Teamsheet> currentTeamsheets = fsContext.loadTeamsheets(newCurrent);

        if(currentTeamsheets.size() == 0) {
            currentTeamsheets = lastTeamsheets;
        }

        for(String manager : currentTeamsheets.keySet()) {
            //Store last month team sheets
            Teamsheet t = currentTeamsheets.get(manager);
            if(t == null) {
                t = lastTeamsheets.get(manager);
            }

            currentTeamsheets.put(manager, t);
            fsContext.storeTeamsheet(t, manager, newCurrent.getGameMonth(), newCurrent.getGameWeek());

            //Also store these to the game month directory
            if(newCurrent.getGameMonth() != oldCurrent.getGameMonth()) {
                fsContext.storeTeamsheet(t, manager, newCurrent.getGameMonth());
            }
        }

        for(String m : currentTeamsheets.keySet()) {
            fsContext.storeTeamsheet(currentTeamsheets.get(m), m, newCurrent.getGameMonth(), newCurrent.getGameWeek());
        }

        return newCurrent;

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
