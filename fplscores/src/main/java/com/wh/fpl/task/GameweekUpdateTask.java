package com.wh.fpl.task;

import com.wh.fpl.FPLApplicationConfig;
import com.wh.fpl.control.CheckGameweek;
import com.wh.fpl.core.FSContext;
import com.wh.fpl.core.Gameweek;
import com.wh.fpl.core.GameweekContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class GameweekUpdateTask {

    private static final Logger LOG = Logger.getLogger(GameweekUpdateTask.class);

    @Autowired
    private FPLApplicationConfig config;

    @Autowired
    private GameweekContext gameweekContext;

    @Scheduled(fixedRate=900000)
    public void updateGameweek() {

        try {
            LOG.info("Updating active gameweek");

            if (gameweekContext != null && gameweekContext.getActiveGameweek() != null) {

                FSContext fs = new FSContext(config.getDataRoot());

                CheckGameweek checkGameweek = new CheckGameweek(gameweekContext.getGameMonth(), gameweekContext.getGameWeek());
                Gameweek gw = checkGameweek.update(gameweekContext.getActiveGameweek());
                fs.storeGameweek(gw);
                gameweekContext.setActiveGameweek(gw);

            }
        } catch (Exception e) {
            LOG.error("Failed to update active gameweek", e);
        }

    }

}
