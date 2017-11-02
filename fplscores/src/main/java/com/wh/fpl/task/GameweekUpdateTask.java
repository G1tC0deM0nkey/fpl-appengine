package com.wh.fpl.task;

import com.wh.fpl.FPLApplicationConfig;
import com.wh.fpl.control.CheckGameweek;
import com.wh.fpl.core.*;
import com.wh.fpl.template.MatchTemplate;
import com.wh.fpl.template.ScoreTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameweekUpdateTask {

    private static final Logger LOG = Logger.getLogger(GameweekUpdateTask.class);

    @Autowired
    private FPLApplicationConfig config;

    @Autowired
    private GameweekContext gameweekContext;

    private Map<String, String> userEmails = new HashMap<String, String>();
    private Map<String, String> userCache = new HashMap<String, String>();

    public GameweekUpdateTask() {
        //TODO: Add User Emails
    }

    @Scheduled(fixedRate=900000)
    public void updateGameweek() {

        try {

            Thread.sleep((int) Math.abs(Math.random() * 300000));

            LOG.info("Updating active gameweek");

            if (gameweekContext != null && gameweekContext.getActiveGameweek() != null) {

                FSContext fs = new FSContext(config.getDataRoot());

                CheckGameweek checkGameweek = new CheckGameweek(gameweekContext.getGameMonth(), gameweekContext.getGameWeek());
                Gameweek gw = checkGameweek.update(gameweekContext.getActiveGameweek());
                fs.storeGameweek(gw);
                gameweekContext.setActiveGameweek(gw);

                createAndSendSummary(fs, gw);

            }
        } catch (Exception e) {
            LOG.error("Failed to update active gameweek", e);
        }

    }

    private void createAndSendSummary(FSContext fs, Gameweek gw) throws Exception {
        Fixtures fixtures = new Fixtures(fs.loadFixtures());

        List<Match> matches = createMatches(fs, gw, fixtures);
        String matchSummary = createMatchSummary(gw, fixtures, matches);

        LOG.info("Notifying users ...");

        for(String user : userEmails.keySet()) {

            LOG.info("Notifying user ... " + user);
            Match userMatch = null;

            //Find user match
            for(Match m : matches) {
                if(m.getFixture().getHome().equals(user) || m.getFixture().getAway().equals(user)) {
                    userMatch = m;
                    break;
                }
            }

            if(userMatch != null) {

                String homeScore = createTeamSummary(gw, userMatch.getHomeTeamScore(), userMatch.getFixture().getHome());
                String awayScore = createTeamSummary(gw, userMatch.getAwayTeamScore(), userMatch.getFixture().getAway());

                StringBuilder sb = new StringBuilder(matchSummary);
                sb.append(homeScore);
                sb.append(awayScore);

                String content = sb.toString();

                String cached = userCache.get(user);
                if (cached == null || !cached.equals(content)) {
                    userCache.put(user, content);

                    String email = userEmails.get(user);

                    try {
                        GameweekUpdateMailer mailer = new GameweekUpdateMailer();
                        mailer.send("[FPL] Gameweek " + gw.getGameWeek() + " Update",
                                content, email);
                    } catch(Exception e) {
                        LOG.error("Error sending update - " + e.getMessage(), e);
                    }

                }
            }

        }
    }

    private String createTeamSummary(Gameweek gw, TeamScore homeTeamScore, String home) {
        ScoreTemplate homeScore = new ScoreTemplate();
        homeScore.setGameweek(gw);
        homeScore.setTeamScore(homeTeamScore);
        homeScore.setName(home);
        return homeScore.render();
    }

    private String createMatchSummary(Gameweek gw, Fixtures fixtures, List<Match> matches) {
        MatchTemplate matchTemplate = new MatchTemplate();
        matchTemplate.setFixtures(fixtures);
        matchTemplate.setGameWeek(gw.getGameWeek());
        matchTemplate.setGameMonth(gw.getGameMonth());
        matchTemplate.setUpdateStatus("Updated");
        matchTemplate.setMatches(matches);

        return matchTemplate.render();
    }

    private List<Match> createMatches(FSContext fs, Gameweek gw, Fixtures fixtures) throws Exception {
        List<Match> matches = new ArrayList<Match>();

        for(Fixture f : fixtures.listFixtures(gw.getGameMonth(), gw.getGameWeek())) {
            Match m = new Match();
            m.setGameweek(gw);
            m.setFixture(f);
            m.setHomeTeamsheet(fs.loadTeamsheet(f.getHome(), gw));
            m.setAwayTeamsheet(fs.loadTeamsheet(f.getAway(), gw));
            matches.add(m);
        }
        return matches;
    }

}
