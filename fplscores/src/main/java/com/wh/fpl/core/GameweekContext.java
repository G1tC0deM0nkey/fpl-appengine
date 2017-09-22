package com.wh.fpl.core;

import org.springframework.stereotype.Component;

@Component
public class GameweekContext {

    private Gameweek gameweek;

    public int getGameMonth() {
        return gameweek.getGameMonth();
    }

    public int getGameWeek() {
        return gameweek.getGameWeek();
    }

    public Gameweek getActiveGameweek() {
        return gameweek;
    }

    public void setActiveGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }
}
