package com.wh.fpl.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jkaye on 29/08/17.
 */
public class PlayerParserTest {

    @Test
    public void testPlayerParsing() throws Exception {

        PlayerParser playerParser = new PlayerParser("https://fantasy.premierleague.com/player-list/");

        Assert.assertNotNull(playerParser.getPlayers());
        Assert.assertEquals(501, playerParser.getPlayers().size());

        int [] count = {0, 0, 0, 0};

        for(Player p : playerParser.getPlayers()) {

            if("GKP".equals(p.getPosition())) {
                count[0]++;
            } else if("DEF".equals(p.getPosition())) {
                count[1]++;
            } else if("MID".equals(p.getPosition())) {
                count[2]++;
            } else {
                count[3]++;
            }

        }

        Assert.assertEquals(52, count[0]);
        Assert.assertEquals(174, count[1]);
        Assert.assertEquals(206, count[2]);
        Assert.assertEquals(69, count[3]);

    }

}
