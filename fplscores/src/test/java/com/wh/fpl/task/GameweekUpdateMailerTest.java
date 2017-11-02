package com.wh.fpl.task;

import org.junit.Test;

/**
 * Created by jkaye on 02/11/17.
 */
public class GameweekUpdateMailerTest {

    @Test
    public void testMailer() {
        GameweekUpdateMailer mailer = new GameweekUpdateMailer();
        mailer.send("Test Message", "Some Content", "john.kw.kaye@gmail.com");
    }

}
