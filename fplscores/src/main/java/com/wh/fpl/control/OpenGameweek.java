package com.wh.fpl.control;

import com.wh.fpl.core.*;

import java.io.PrintWriter;
import java.util.List;

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
        Gameweek gw = openGameweek.open();

        FSContext context = new FSContext("data");
        context.storeGameweek(gw);

        List <Fixture> fixtures = context.loadFixtures(gw);
        context.storeFixtures(gw, fixtures);

        System.exit(0);

    }
}
