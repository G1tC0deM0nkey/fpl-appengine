package com.wh.fpl.control;

import com.wh.fpl.core.Player;
import com.wh.fpl.core.PlayerParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by johnk on 24/01/2018.
 */
public class PlayerValue {

    private static final Map<String, Double> FIXTURE_COEFFS = new HashMap<String, Double>();

    static {
        FIXTURE_COEFFS.put("Man City",1.9572864d);
        FIXTURE_COEFFS.put("Man Utd",2d);
        FIXTURE_COEFFS.put("Chelsea",1.919597d);
        FIXTURE_COEFFS.put("Liverpool",1.839195d);
        FIXTURE_COEFFS.put("Spurs",1.907035d);
        FIXTURE_COEFFS.put("Arsenal",1.615577d);
        FIXTURE_COEFFS.put("Leicester",1.723618d);
        FIXTURE_COEFFS.put("Burnley",1.723618d);
        FIXTURE_COEFFS.put("Everton",1.575376d);
        FIXTURE_COEFFS.put("Watford",1.4221102d);
        FIXTURE_COEFFS.put("West Ham",1.246231159d);
        FIXTURE_COEFFS.put("Bournemouth",1.4949748d);
        FIXTURE_COEFFS.put("Crystal Palace",1.44974874d);
        FIXTURE_COEFFS.put("Huddersfield",1.1080402d);
        FIXTURE_COEFFS.put("Newcastle",1.0025127d);
        FIXTURE_COEFFS.put("Brighton",1.1331658d);
        FIXTURE_COEFFS.put("Stoke",1d);
        FIXTURE_COEFFS.put("Southampton",1.2211055d);
        FIXTURE_COEFFS.put("West Brom",1.0075376d);
        FIXTURE_COEFFS.put("Swansea",1.296482d);
    }

    public static void main(String [] args) throws Exception {

        PlayerParser playerParser = new PlayerParser("https://fantasy.premierleague.com/player-list/");
        List<Player> players = playerParser.getPlayers();

        List<PlayerValueCoeff> gkCoeffs = new ArrayList<PlayerValueCoeff>();
        List<PlayerValueCoeff> defCoeffs = new ArrayList<PlayerValueCoeff>();
        List<PlayerValueCoeff> midCoeffs = new ArrayList<PlayerValueCoeff>();
        List<PlayerValueCoeff> fwdCoeffs = new ArrayList<PlayerValueCoeff>();
        Map<String, List<PlayerValueCoeff>> coeffs = new HashMap<String, List<PlayerValueCoeff>>();
        coeffs.put("GKP", gkCoeffs);
        coeffs.put("DEF", defCoeffs);
        coeffs.put("MID", midCoeffs);
        coeffs.put("FWD", fwdCoeffs);

        for(Player p : players) {
            try {
                Double points = Double.parseDouble(p.getScore());
                Double coeff = points * FIXTURE_COEFFS.get(p.getTeam());
                PlayerValueCoeff pvc = new PlayerValueCoeff(p, points, coeff);
                coeffs.get(p.getPosition()).add(pvc);
            } catch(Exception e) {
                System.err.println("Error for " + p.getName() + " of " + p.getTeam() + "(" + p.getPosition() + ")");
                e.printStackTrace();
            }
        }

        Set<String> ap = availablePlayers();
        gkCoeffs = sort(filter(gkCoeffs, ap));
        defCoeffs = sort(filter(defCoeffs, ap));
        midCoeffs = sort(filter(midCoeffs, ap));
        fwdCoeffs = sort(filter(fwdCoeffs, ap));

        print("Goalkeepers", gkCoeffs);
        print("Defenders", defCoeffs);
        print("Midfielders", midCoeffs);
        print("Forwards", fwdCoeffs);

    }

    private static List <PlayerValueCoeff> filter(List<PlayerValueCoeff> list, Set <String> available) {

        List <PlayerValueCoeff> ap = new ArrayList<PlayerValueCoeff>();

        for(PlayerValueCoeff pvc : list) {
            for(String pl : available) {
                if(pl.contains(pvc.player.getName())) {
                    ap.add(pvc);
                }
            }
        }

        return ap;
    }

    private static void print(String message, List <PlayerValueCoeff> list) {

        System.out.println();
        System.out.println("== " + message + " ==");
        System.out.println();

        for(PlayerValueCoeff p : list) {
            System.out.println(p.player.getTeam() + "\t" + p.player.getName() + "\t" + p.player.getScore() + "\t" + p.value);
        }
    }

    private static List <PlayerValueCoeff> sort(List<PlayerValueCoeff> list) {

        Collections.sort(list, new Comparator<PlayerValueCoeff>() {
            @Override
            public int compare(PlayerValueCoeff o1, PlayerValueCoeff o2) {
                int o1m = (int)(100.0d * o1.value);
                int o2m = (int)(100.0d * o2.value);
                return o2m - o1m;
            }
        });

        return list;
    }

    static class PlayerValueCoeff {

        public PlayerValueCoeff(Player player, Double points, Double value) {
            this.player = player;
            this.points = points;
            this.value = value;
        }

        private Player player;

        private Double points;

        private Double value;

    }

    private static Set <String> availablePlayers() throws Exception {

        InputStream stream = PlayerValue.class.getResourceAsStream("/transfers.february.2018.txt");
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(isr);

        String line = br.readLine();
        Set <String> players = new HashSet<String>();

        while(line != null) {

            if(line.length() > 5 && !FIXTURE_COEFFS.containsKey(line)) {
                players.add(line);
            }

            line = br.readLine();
        }

        return players;
    }

}
