package com.wh.fpl.core;

import com.wh.fpl.utils.SimpleNamer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created by jkaye on 20/09/17.
 */
public class FSContext {

    private Logger LOG = LogManager.getLogger(FSContext.class);

    private String root = ".";

    public FSContext(String root) {
        this.root = root;
    }

    public void storeGameweek(Gameweek gw) throws IOException {

        File gameweekData = new File(root + "/gm" + gw.getGameMonth() + "/gw" + gw.getGameWeek() + "/players.tsv");

        LOG.info("Storing "+ gameweekData.getPath());

        if(!gameweekData.getParentFile().exists()) {
            gameweekData.getParentFile().mkdirs();
        }

        FileWriter fw = new FileWriter(gameweekData);
        PrintWriter pw = new PrintWriter(fw);

        gw.export(pw);

        pw.close();

    }

    public Gameweek loadGameweek(int gameMonth, int gameWeek) throws IOException {

        Gameweek gw = new Gameweek(gameMonth, gameWeek);

        File gameweekData = new File(root + "/gm" + gameMonth + "/gw" + gameWeek + "/players.tsv");
        if(gameweekData.exists()) {

            LOG.info("Loading " + gameweekData.getPath());

            //Parse it
            InputStream is = new FileInputStream(gameweekData);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();

            while(line != null) {

                String [] tokens = line.split("\t");

                if(tokens.length != 8) {
                    throw new IllegalStateException("GameweekConstants Data is incorrect - found (" + tokens.length + ") " + line);
                }

                gw.update(tokens);

                line = br.readLine();
            }

        } else {
            LOG.info("No data to load at " + gameweekData.getPath());
        }

        return gw;

    }

    public List <Fixture> loadFixtures(Gameweek gw) throws IOException {

        List<Fixture> fixtures = new ArrayList<Fixture>();

        File f = gw == null
                ? new File(root + "/fixtures.tsv")
                : new File(root + "/gm" + gw.getGameMonth() + "/gw" + gw.getGameWeek() + "/fixtures.tsv");

        LOG.info("Loading " + f.getPath());

        if(!f.exists()) {

            List <Fixture> allFixtures = loadFixtures();

            for(Fixture fix : allFixtures) {
                if(fix.getGameMonth() == gw.getGameMonth() && fix.getGameWeek() == gw.getGameWeek()) {
                    fixtures.add(fix);
                }
            }

            return fixtures;

        }

        InputStream is = new FileInputStream(f);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line = br.readLine();

        while(line != null) {

            String [] tokens = line.split("\t");

            if(tokens.length != 4) {
                throw new IllegalStateException("Fixtures are incorrect - expected four tokens <gm> <gw> <home> <away> but found (" + tokens.length + ") " + line);
            }

            Fixture fix = new Fixture();
            fix.setGameMonth(Integer.parseInt(tokens[0]));
            fix.setGameWeek(Integer.parseInt(tokens[1]));
            fix.setHome(tokens[2]);
            fix.setAway(tokens[3]);

            fixtures.add(fix);

            line = br.readLine();
        }

        return fixtures;

    }

    public List <Fixture> loadFixtures() throws IOException {
        return loadFixtures(null);
    }

    public void storeFixtures(Gameweek gw, List <Fixture> fixtures) throws IOException {

        File f = new File(root +
                (gw == null ? "" : ("/gm" + gw.getGameMonth() + "/gw" + gw.getGameWeek()))
                + "/fixtures.tsv");

        LOG.info("Storing " + f.getPath());

        if(!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        FileWriter fw = new FileWriter(f);
        PrintWriter pw = new PrintWriter(fw);

        for(Fixture fix : fixtures) {

            StringBuilder sb = new StringBuilder();
            sb.append(fix.getGameMonth()).append("\t");
            sb.append(fix.getGameWeek()).append("\t");
            sb.append(fix.getHome()).append("\t");
            sb.append(fix.getAway());

            pw.println(sb.toString());
        }

        pw.flush();
        pw.close();

    }

    public List <PlayerKey> loadSquad(String name) throws Exception {

        File f = new File(root +
                ("/squads/" + SimpleNamer.simpleName(name) + ".tsv"));

        if(!f.exists()) {
            LOG.info("No squad information at " + f.getPath());
            return null;
        }

        LOG.info("Loading " + f.getPath());

        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        List <PlayerKey> players = new ArrayList<PlayerKey>();
        String line = br.readLine();

        while(line != null) {
            String [] tokens = line.split("\t");

            PlayerKey k = new PlayerKey(tokens[2], tokens[0], tokens[1]);
            players.add(k);

            line = br.readLine();
        }

        return players;
    }

    public Teamsheet loadTeamsheet(String name, Gameweek gameweek) throws Exception {

        File f = new File(root +
                ("/gm" + gameweek.getGameMonth() + "/gw" + gameweek.getGameWeek() + "/" + SimpleNamer.simpleName(name) + ".tsv"));

        if(!f.exists()) {
            return null;
        }

        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        Teamsheet teamsheet = new Teamsheet();
        teamsheet.setGamemonth(gameweek.getGameMonth());

        String line = br.readLine();

        while(line != null && !"".equals(line)) {
            String [] tokens = line.split("\t");

            if(tokens.length == 0) {
                break;
            }

            String player = tokens[2];
            String playerTeam = tokens[0];
            String playerPosition = tokens[1];

            boolean captain = false;
            boolean vice = false;

            if(tokens.length > 3) {
                String info = tokens[3];

                if ("C".equals(info)) {
                    captain = true;
                } else if ("VC".equals(info)) {
                    vice = true;
                }
            }

            PlayerKey playerKey = new PlayerKey(player, playerTeam, playerPosition);

            if(teamsheet.getStarters().size() >= 11) {
                teamsheet.getSubs().add(playerKey);
            }
            else {
                teamsheet.getStarters().add(playerKey);
            }

            if(captain) {
                teamsheet.setCaptain(playerKey);
            } else if(vice) {
                teamsheet.setVice(playerKey);
            }

            line = br.readLine();
        }

        return teamsheet;
    }


    public Teamsheet loadTeamsheet(String name, int gameMonth) throws Exception {

        File f = new File(root +
                ("/gm" + gameMonth + "/" + SimpleNamer.simpleName(name) + ".tsv"));

        LOG.info("Loading " + f.getPath());

        if(!f.exists()) {
            return null;
        }

        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        Teamsheet teamsheet = new Teamsheet();
        teamsheet.setGamemonth(gameMonth);

        String line = br.readLine();

        while(line != null && !"".equals(line)) {
            String [] tokens = line.split("\t");

            if(tokens.length == 0) {
                break;
            }

            String player = tokens[2];
            String playerTeam = tokens[0];
            String playerPosition = tokens[1];

            boolean captain = false;
            boolean vice = false;

            if(tokens.length > 3) {
                String info = tokens[3];

                if ("C".equals(info)) {
                    captain = true;
                } else if ("VC".equals(info)) {
                    vice = true;
                }
            }

            PlayerKey playerKey = new PlayerKey(player, playerTeam, playerPosition);

            if(teamsheet.getStarters().size() >= 11) {
                teamsheet.getSubs().add(playerKey);
            }
            else {
                teamsheet.getStarters().add(playerKey);
            }

            if(captain) {
                teamsheet.setCaptain(playerKey);
            } else if(vice) {
                teamsheet.setVice(playerKey);
            }

            line = br.readLine();
        }

        return teamsheet;
    }

    public void storeTeamsheet(Teamsheet teamsheet, String name, int gameMonth, int gameweek) throws Exception {

        File f = new File(root +
                ("/gm" + gameMonth + "/gw" + gameweek + "/" + SimpleNamer.simpleName(name) + ".tsv"));

        if(!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        FileWriter fw = new FileWriter(f);
        PrintWriter pw = new PrintWriter(fw);

        for(PlayerKey pk : teamsheet.getStarters()) {
            StringBuilder sb = new StringBuilder();
            sb.append(pk.getTeam()).append("\t");
            sb.append(pk.getPosition()).append("\t");
            sb.append(pk.getName()).append("\t");

            if(pk.equals(teamsheet.getCaptain())) {
                sb.append("C");
            } else if (pk.equals(teamsheet.getVice())) {
                sb.append("VC");
            }

            pw.println(sb.toString());

        }

        pw.flush();
        pw.close();

    }

    public void storeTeamsheet(Teamsheet teamsheet, String name, int gameMonth) throws Exception {

        File f = new File(root +
                ("/gm" + gameMonth + "/" + SimpleNamer.simpleName(name) + ".tsv"));

        if(!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        FileWriter fw = new FileWriter(f);
        PrintWriter pw = new PrintWriter(fw);

        for(PlayerKey pk : teamsheet.getStarters()) {
            StringBuilder sb = new StringBuilder();
            sb.append(pk.getTeam()).append("\t");
            sb.append(pk.getPosition()).append("\t");
            sb.append(pk.getName()).append("\t");

            if(pk.equals(teamsheet.getCaptain())) {
                sb.append("C");
            } else if (pk.equals(teamsheet.getVice())) {
                sb.append("VC");
            }

            pw.println(sb.toString());

        }

        pw.flush();
        pw.close();

    }

    public void storeSquad(String name, Set <PlayerKey> squad,
                           Map <String, Map <String, Set<String>>> teamPositionNameMap) throws Exception {

        File f = new File(root +
                ("/squads/" + SimpleNamer.simpleName(name) + ".tsv"));

        if(!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        List <PlayerKey> players = new ArrayList<PlayerKey>(squad);
        Collections.sort(players, new PlayerPositionComparator());

        FileWriter fw = new FileWriter(f);
        PrintWriter pw = new PrintWriter(fw);

        for(PlayerKey p : players) {
            String playerName = p.getName();

            Map <String, Set <String>> playersByPosition = teamPositionNameMap.get(p.getTeam());
            Set <String> candidates = playersByPosition == null ? null : teamPositionNameMap.get(p.getTeam()).get(p.getPosition());

            if(candidates != null && !candidates.contains(playerName))
            {
                for(String c : candidates) {
                    if(playerName.contains(c)) {
                        playerName = c;
                        break;
                    }
                }
            }

            pw.println(String.format("%s\t%s\t%s", p.getTeam(), p.getPosition(), playerName));
        }

        pw.flush();
        pw.close();

    }

    public Map<String, Teamsheet> loadTeamsheets(Gameweek gw) throws Exception {

        List <Fixture> fixtures = loadFixtures(gw);
        Map <String, Teamsheet> teamsheets = new HashMap<String, Teamsheet>();

        for(Fixture f : fixtures) {

            String home = f.getHome();
            Teamsheet homeTeam = loadTeamsheet(home, gw.getGameMonth());
            teamsheets.put(home, homeTeam);

            String away = f.getAway();
            Teamsheet awayTeam = loadTeamsheet(away, gw.getGameMonth());
            teamsheets.put(away, awayTeam);

        }

        return teamsheets;

    }

}
