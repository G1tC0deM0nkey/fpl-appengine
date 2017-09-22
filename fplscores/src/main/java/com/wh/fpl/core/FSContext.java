package com.wh.fpl.core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jkaye on 20/09/17.
 */
public class FSContext {

    private String root = ".";

    public FSContext(String root) {
        this.root = root;
    }

    public void storeGameweek(Gameweek gw) throws IOException {

        File gameweekData = new File(root + "/gm" + gw.getGameMonth() + "/gw" + gw.getGameWeek() + "/players.tsv");

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

        }

        return gw;

    }

    public List <Fixture> loadFixtures(Gameweek gw) throws IOException {

        List<Fixture> fixtures = new ArrayList<Fixture>();

        File f = gw == null
                ? new File(root + "/fixtures.tsv")
                : new File(root + "/gm" + gw.getGameMonth() + "/gw" + gw.getGameWeek() + "/fixtures.tsv");

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

}
