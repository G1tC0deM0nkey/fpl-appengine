package com.wh.fpl.web;

import com.sun.org.apache.regexp.internal.RE;
import com.wh.fpl.FPLApplicationConfig;
import com.wh.fpl.control.CheckGameweek;
import com.wh.fpl.control.GameweekConstants;
import com.wh.fpl.control.OpenGameweek;
import com.wh.fpl.core.*;
import com.wh.fpl.template.MatchTemplate;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jkaye on 21/09/17.
 */
@Controller
public class PlayersController implements ApplicationContextAware {

    private Logger LOG = LogManager.getLogger(PlayersController.class);

    @Autowired
    private FPLApplicationConfig config;

    @Autowired
    private GameweekContext gameweekContext;

    public PlayersController() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        gameweekContext.setActiveGameweek(new Gameweek(GameweekConstants.MONTH, GameweekConstants.WEEK));
    }

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

    @RequestMapping(value="/squad", method=RequestMethod.GET)
    public @ResponseBody String squads(@RequestParam String name) throws Exception {

        FSContext fs = new FSContext(config.getDataRoot());

        List <PlayerKey> squad = fs.loadSquad(name);

        StringBuilder sb = new StringBuilder();

        for(PlayerKey p : squad) {
            sb.append(p.getTeam() + " - " + p.getPosition() + " - " + p.getName() + "<br/>\n");
        }

        return sb.toString();

    }

    @RequestMapping(value="/score", method=RequestMethod.GET)
    public @ResponseBody String score(@RequestParam String name, @RequestParam(defaultValue="0") int month, @RequestParam(defaultValue ="0") int week) throws Exception {

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
        } else {
            gameweek = gw;
        }

        List <PlayerKey> squad = fs.loadSquad(name);

        StringBuilder sb = new StringBuilder();
        int total = 0;

        for(PlayerKey p : squad) {
            sb.append(p.getTeam() + " - " + p.getPosition() + " - " + p.getName());

            Player pl1 = gameweek.getStartingScores().get(p);
            Player pl2 = gameweek.getLatestScores().get(p);

            if(pl1 != null) {
                int score = Integer.parseInt(pl2.getScore()) - Integer.parseInt(pl1.getScore());
                total += score;
                sb.append(" - ").append(score);
                if(pl2.isPlaying()) {
                    sb.append(" - playing");
                }
            }

            sb.append("<br/>\n");
        }

        sb.append("<br/>\nTotal : ").append(total);

        return sb.toString();

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

    @RequestMapping(value="/matches", method=RequestMethod.GET)
    public @ResponseBody String getMatches(@RequestParam(defaultValue = "0") int month, @RequestParam(defaultValue = "0") int week) throws Exception {

        month = month == 0 ? gameweekContext.getGameMonth() : month;
        week = week == 0 ? gameweekContext.getGameWeek() : week;

        LOG.info("Get matches m " + month + " w " + week);

        FSContext fs = new FSContext(config.getDataRoot());
        MatchTemplate template = new MatchTemplate();

        template.setGameMonth(month);
        template.setGameWeek(week);

        Fixtures fixtures = new Fixtures(fs.loadFixtures());
        template.setFixtures(fixtures);

        Gameweek gameweek = fs.loadGameweek(month, week);
        if(month == gameweekContext.getGameMonth() && week == gameweekContext.getGameWeek()) {
            try {
                CheckGameweek check = new CheckGameweek(gameweek.getGameMonth(), gameweek.getGameWeek());
                gameweek = check.update(gameweek);
                fs.storeGameweek(gameweek);
            }
            catch(Exception e) {
                template.setUpdateStatus("Failed to update - " + e.getMessage());
            }
        }

        Map <String, Teamsheet> teamsheets = fs.loadTeamsheets(gameweek);

        List <Fixture> weekFixtures = fixtures.listFixtures(month, week);
        List <Match> weekMatches = new ArrayList<Match>();

        for(Fixture f : weekFixtures) {

            Match match = new Match();

            match.setFixture(f);
            match.setGameweek(gameweek);

            match.setHomeTeamsheet(teamsheets.get(f.getHome()));
            match.setAwayTeamsheet(teamsheets.get(f.getAway()));

            weekMatches.add(match);
        }

        template.setMatches(weekMatches);

        return template.render();
    }


}
