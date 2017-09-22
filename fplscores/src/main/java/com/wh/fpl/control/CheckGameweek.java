package com.wh.fpl.control;

import com.wh.fpl.core.FSContext;
import com.wh.fpl.core.Gameweek;
import com.wh.fpl.core.Player;
import com.wh.fpl.core.PlayerParser;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by jkaye on 18/09/17.
 */
public class CheckGameweek {

    private int gameMonth;

    private int gameweek;

    public CheckGameweek(int gameMonth, int gameweek) {
        this.gameMonth = gameMonth;
        this.gameweek = gameweek;
    }

    public Gameweek update() throws Exception {
        Gameweek gw = new Gameweek(gameMonth, gameweek);
        return update(gw);
    }

    public Gameweek update(Gameweek gw) throws Exception {

        PlayerParser playerParser = new PlayerParser("https://fantasy.premierleague.com/player-list/");
        List<Player> players = playerParser.getPlayers();

        for (Player p : players) {
            Player toUpdate = gw.getLatestScores().get(p.getPlayerKey());
            if(toUpdate != null) {
                toUpdate.update(p);
            }
            else {
                gw.getLatestScores().put(p.getPlayerKey(), p);
            }
        }

        gw.export(new PrintWriter(System.out));

        return gw;
    }

    public static void main(String [] args) throws Exception {

        CheckGameweek checkGameweek = new CheckGameweek(GameweekConstants.MONTH, GameweekConstants.WEEK);
        Gameweek gw = checkGameweek.update();

        FSContext context = new FSContext("data");
        context.storeGameweek(gw);

        System.exit(0);

    }

}
