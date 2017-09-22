package com.wh.fpl.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jkaye on 29/08/17.
 */
public class SimpleParserTest {

    private String document;

    @Before
    public void setup() throws Exception {

        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        URLConnection conn = new URL("https://fantasy.premierleague.com/player-list/").openConnection();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        StringBuffer sb = new StringBuffer();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        br.close();

        document = sb.toString();
    }

    @Test
    public void testSimpleParsing() throws Exception {

        SimpleParser parser = new SimpleParser(document);

        String gk = parser.open("<h2>Goalkeepers</h2>").close("<h2>Defenders</h2>");
        String df = parser.open("<h2>Defenders</h2>").close("<h2>Midfielders</h2>");
        String mf = parser.open("<h2>Midfielders</h2>").close("<h2>Forwards</h2>");
        String fwd = parser.open("<h2>Forwards</h2>").close(null);

        Assert.assertNotNull(gk);
        Assert.assertNotNull(df);
        Assert.assertNotNull(mf);
        Assert.assertNotNull(fwd);

    }

}
