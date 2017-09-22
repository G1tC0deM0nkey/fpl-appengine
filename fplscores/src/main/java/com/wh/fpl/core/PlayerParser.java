package com.wh.fpl.core;

import com.wh.fpl.utils.SimpleParser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jkaye on 29/08/17.
 */
public class PlayerParser {

    private String url;

    private List<Player> players;

    public PlayerParser(String url) throws Exception {
        this.players = new ArrayList<Player>();
        this.url = url;
        setup();
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void setup() throws Exception {

        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        URLConnection conn = new URL(url).openConnection();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        StringBuffer sb = new StringBuffer();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        br.close();

        SimpleParser parser = new SimpleParser(sb.toString());

        String gk = parser.open("<h2>Goalkeepers</h2>").close("<h2>Defenders</h2>");
        String df = parser.close("<h2>Midfielders</h2>");
        String mf = parser.close("<h2>Forwards</h2>");
        String fwd = parser.close(null);

        processPlayers("GK", new SimpleParser(gk), players);
        processPlayers("DEF", new SimpleParser(df), players);
        processPlayers("MID", new SimpleParser(mf), players);
        processPlayers("FWD", new SimpleParser(fwd), players);

    }


    private void processPlayers(String position, SimpleParser parser, List <Player> players) {

        String field = parser.open("<td>").close("</td>");
        List <String> fields = new ArrayList<String>();

        while(field != null) {

            fields.add(field);

            if(fields.size() == 4) {
                Player p = new Player();
                p.setName(fields.get(0));
                p.setTeam(fields.get(1));
                p.setScore(fields.get(2));
                p.setValue(fields.get(3));
                p.setPosition(position);
                players.add(p);
                fields.clear();
            }

            field = parser.open("<td>").close("</td>");

        }

    }

}