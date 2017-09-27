package com.wh.fpl.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by johnk on 27/09/2017.
 */
public class Fixtures {

    private List<Fixture> fixtures;

    private Map<Integer, Integer> weekToMonth;

    public Fixtures(List<Fixture> fixtures) {
        this.fixtures = fixtures;
        this.weekToMonth = new HashMap<Integer, Integer>();

        for(Fixture f : fixtures) {
            weekToMonth.put(f.getGameWeek(), f.getGameMonth());
        }
    }

    public List <Fixture> listFixtures(int gameMonth, int gameWeek) {

        List <Fixture> mwFixtures = new ArrayList<Fixture>();

        for(Fixture fix : fixtures) {
            if(fix.getGameMonth() == gameMonth && fix.getGameWeek() == gameWeek) {
                mwFixtures.add(fix);
            }
        }

        return mwFixtures;

    }

    public int nextGamemonth(int gameweek) {
        return gameweek == 38 ? 10 : weekToMonth.get(gameweek+1);
    }

    public int nextGameweek(int gameweek) {
        return gameweek == 38 ? 38 : gameweek+1;
    }

    public int prevGamemonth(int gameweek) {
        return gameweek == 1 ? 1 : weekToMonth.get(gameweek-1);
    }

    public int prevGameweek(int gameweek) {
        return gameweek == 1 ? 1 : gameweek-1;
    }


}
