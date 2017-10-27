package com.wh.fpl.core;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by johnkon 27/10/2017.
 */
public enum Formation {

    _3_5_2(1,3,5,2),
    _3_4_3(1,3,4,3),
    _4_5_1(1,4,5,1),
    _4_3_3(1,4,3,3),
    _4_4_2(1,4,4,2),
    _5_4_1(1,5,4,1),
    _5_3_2(1,5,3,2)

    ;

    int nGoalkeepers;
    int nDefenders;
    int nMidfielders;
    int nForwards;

    private Formation(int nGkp, int nDef, int nMid, int nFwd) {
        this.nGoalkeepers = nGkp;
        this.nDefenders = nDef;
        this.nMidfielders = nMid;
        this.nForwards = nFwd;
    }

    public static boolean validFormation(List<PlayerKey> players) {

        if(countPlayers(players) != 11) {
            return false;
        }

        if(countPositions(players, "GKP") != 1) {
            return false;
        }

        if(countPositions(players, "DEF") < 3 || countPositions(players, "DEF") > 5) {
            return false;
        }

        if(countPositions(players, "MID") < 3 || countPositions(players, "MID") > 5) {
            return false;
        }

        if(countPositions(players, "FWD") < 1 || countPositions(players, "FWD") > 3) {
            return false;
        }

        return true;

    }

    public static Formation getFormation(List <PlayerKey> players) {
        if(validFormation(players)) {
            HashSet <Formation> formations = new HashSet<Formation>(EnumSet.allOf(Formation.class));

            removeFormationsWithoutDefenders(formations, countPositions(players, "DEF"));
            removeFormationsWithoutMidfielders(formations, countPositions(players, "MID"));
            removeFormationsWithoutForwards(formations, countPositions(players, "FWD"));

            if(formations.size() == 1) {
                return formations.iterator().next();
            }
        }

        return null;
    }

    private static void removeFormationsWithoutDefenders(Set<Formation> formations, int count) {
        Set <Formation> updatedFormations = new HashSet<Formation>();
        for(Formation f : formations) {
            if(f.nDefenders == count) {
                updatedFormations.add(f);
            }
        }
        formations.clear();
        formations.addAll(updatedFormations);
    }

    private static void removeFormationsWithoutMidfielders(Set<Formation> formations, int count) {
        Set <Formation> updatedFormations = new HashSet<Formation>();
        for(Formation f : formations) {
            if(f.nMidfielders == count) {
                updatedFormations.add(f);
            }
        }
        formations.clear();
        formations.addAll(updatedFormations);
    }

    private static void removeFormationsWithoutForwards(Set<Formation> formations, int count) {
        Set <Formation> updatedFormations = new HashSet<Formation>();
        for(Formation f : formations) {
            if(f.nForwards == count) {
                updatedFormations.add(f);
            }
        }
        formations.clear();
        formations.addAll(updatedFormations);
    }

    private static int countPlayers(List <PlayerKey> players) {
        return players.size();
    }

    private static int countPositions(List <PlayerKey> players, String position) {
        int count = 0;
        for(PlayerKey p : players) {
            if(position.equals(p.getPosition())) {
                count++;
            }
        }
        return count;
    }

}
