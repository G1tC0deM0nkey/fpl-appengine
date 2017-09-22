package com.wh.fpl.control;

import com.wh.fpl.core.FSContext;
import com.wh.fpl.core.Gameweek;
import com.wh.fpl.core.PlayerKey;
import com.wh.fpl.core.Teamsheet;
import com.wh.fpl.utils.SimpleNamer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jkaye on 22/09/17.
 */
public class AssembleSquads {

    public static void main(String [] args) throws Exception {

        FSContext context = new FSContext("data");

        Map<String, Teamsheet> m4 = context.loadTeamsheets(new Gameweek(2,4));
        Map<String, Teamsheet> m5 = context.loadTeamsheets(new Gameweek( 2,5));

        Gameweek gameweek = context.loadGameweek(2, 6);
        Map allPlayers = allPlayersByTeamPositionName(gameweek);

        for(String m : m4.keySet()) {

            String name = m;
            name = SimpleNamer.simpleName(name);

            Teamsheet t4 = m4.get(m);
            Teamsheet t5 = m5.get(m);

            Set<PlayerKey> squad = new HashSet<PlayerKey>();
            squad.addAll(t4.getStarters());
            squad.addAll(t5.getStarters());

            context.storeSquad(name, squad, allPlayers);
        }

    }

    private static final Map allPlayersByTeamPositionName(Gameweek gameweek) {

        Map <String, Map <String, Set <String>>> allPlayers = new HashMap<String, Map <String, Set <String>>>();
        for(PlayerKey p : gameweek.getStartingScores().keySet()) {

            Map <String, Set <String>> allPlayersFromTeam = allPlayers.get(p.getTeam());

            if(allPlayersFromTeam == null) {
                allPlayersFromTeam = new HashMap<String, Set<String>>();
                allPlayers.put(p.getTeam(), allPlayersFromTeam);
            }

            Set <String> allPlayersFromTeamInPosition = allPlayersFromTeam.get(p.getPosition());

            if(allPlayersFromTeamInPosition == null) {
                allPlayersFromTeamInPosition = new HashSet<String>();
                allPlayersFromTeam.put(p.getPosition(), allPlayersFromTeamInPosition);
            }

            allPlayersFromTeamInPosition.add(p.getName());

        }

        return allPlayers;
    }

}
