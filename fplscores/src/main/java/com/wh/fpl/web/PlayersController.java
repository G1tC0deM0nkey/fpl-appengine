package com.wh.fpl.web;

import com.sun.org.apache.regexp.internal.RE;
import com.wh.fpl.FPLApplicationConfig;
import com.wh.fpl.control.CheckGameweek;
import com.wh.fpl.control.GameweekConstants;
import com.wh.fpl.control.OpenGameweek;
import com.wh.fpl.core.FSContext;
import com.wh.fpl.core.Fixture;
import com.wh.fpl.core.Gameweek;
import com.wh.fpl.core.GameweekContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by jkaye on 21/09/17.
 */
@Controller
public class PlayersController {

    @Autowired
    private FPLApplicationConfig config;

    @Autowired
    private GameweekContext gameweekContext;

    @RequestMapping(value="/players-list", method= RequestMethod.GET, produces="text/html")
    public @ResponseBody String getPlayersList(
            @RequestParam(defaultValue="0") int month, @RequestParam(defaultValue="0") int week) throws Exception {

        FSContext fs = new FSContext(config.getDataRoot());

        Gameweek gameweek = gameweekContext.getActiveGameweek();
        boolean active = true;

        if(month != 0 && week != 0) {
            gameweek = new Gameweek(month, week);
            active = (gameweekContext.getActiveGameweek() != null && gameweek.getGameMonth() == gameweekContext.getGameMonth() && gameweek.getGameWeek() == gameweekContext.getGameWeek());
        }
        else if(gameweek == null) {
            return "No active gameweek!";
        }

        //Loaded Gameweek
        Gameweek gw = fs.loadGameweek(gameweek.getGameMonth(), gameweek.getGameWeek());

        //If it's the active gameweek, update and store the result
        if(active) {
            CheckGameweek check = new CheckGameweek(gw.getGameMonth(), gw.getGameWeek());
            gameweek = check.update(gw);
            fs.storeGameweek(gameweek);
        }

        //Output to the response
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        gameweek.print(pw);
        pw.close();

        return sw.toString();

    }

    @RequestMapping(value="/fixtures", method= RequestMethod.GET, produces = "text/html")
    public @ResponseBody String getFixtures(
            @RequestParam(defaultValue="0") int month, @RequestParam(defaultValue="0") int week) throws Exception {

        FSContext fs = new FSContext(config.getDataRoot());

        Gameweek gameweek = gameweekContext.getActiveGameweek();
        if(month != 0 && week != 0) {
            gameweek = new Gameweek(month, week);
        }

        List<Fixture> fixtures = fs.loadFixtures(gameweek);

        StringBuilder sb = new StringBuilder();
        for(Fixture f : fixtures) {
            sb.append("Month ").append(f.getGameMonth()).append(" Week ").append(f.getGameWeek()).append(" - ");
            sb.append(f.getHome()).append(" vs ").append(f.getAway()).append("<br/>\n");
        }

        return sb.toString();

    }

    @RequestMapping(value="/start-gameweek", method=RequestMethod.POST)
    public @ResponseBody String startGameweek(@RequestParam int month, @RequestParam int week) throws Exception {

        FSContext fs = new FSContext(config.getDataRoot());

        OpenGameweek openGameweek = new OpenGameweek(GameweekConstants.MONTH, GameweekConstants.WEEK);
        Gameweek gw = openGameweek.open();
        fs.storeGameweek(gw);

        Gameweek gameweek = fs.loadGameweek(month, week);
        gameweekContext.setActiveGameweek(gameweek);

        return "Month " + month + " Week " + week + " has been started and is now active";

    }


    @RequestMapping(value="/active-gameweek", method=RequestMethod.GET)
    public @ResponseBody String activeGameweek() throws Exception {
        return (gameweekContext.getActiveGameweek() == null)
                ? "Active Gameweek is not set"
                : "Active Gameweek is Month " + gameweekContext.getGameMonth() + " Week " + gameweekContext.getGameWeek();
    }

    @RequestMapping(value="/active-gameweek", method=RequestMethod.POST)
    public @ResponseBody String activateGameweek(@RequestParam int month, @RequestParam int week) throws Exception {
        FSContext fs = new FSContext(config.getDataRoot());
        Gameweek gameweek = fs.loadGameweek(month, week);
        gameweekContext.setActiveGameweek(gameweek);

        return "Month " + month + " Week " + week + " is now active";

    }

}
