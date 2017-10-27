package com.wh.fpl.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnk on 27/10/2017.
 */
public class FormationTest {

    @Test
    public void testValidFormation() throws Exception {

        FSContext ctx = new FSContext("data");

        Teamsheet t = ctx.loadTeamsheet("johnk", 3);

        boolean valid = Formation.validFormation(t.getStarters());

        Assert.assertTrue(valid);
    }

    @Test
    public void testGetFormation() throws Exception {

        FSContext ctx = new FSContext("data");

        Teamsheet t = ctx.loadTeamsheet("johnk", 3);

        Formation f = Formation.getFormation(t.getStarters());

        Assert.assertEquals(Formation._3_4_3, f);
    }


}