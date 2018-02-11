package com.wh.fpl.template;

import com.wh.fpl.core.Gameweek;
import com.wh.fpl.core.Player;
import com.wh.fpl.core.PlayerKey;

import java.util.List;

/**
 * Created by johnk on 11/02/2018.
 */
public class PlayerOrderTemplate {

    private List<PlayerKey> playerOrder;

    private Gameweek gameweek;

    private boolean debug;

    public PlayerOrderTemplate(List<PlayerKey> playerOrder, Gameweek gameweek, boolean debug) {
        this.playerOrder = playerOrder;
        this.gameweek = gameweek;
        this.debug = debug;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for(PlayerKey p : playerOrder) {

            PlayerKey properKey = findProperKey(p);

            if(properKey == null) {
                if(debug) { sb.append(p.getName()).append("\t").append(p.getTeam()).append("\t").append(p.getPosition()).append("\t");}
                sb.append("???\n<br/>");
            }
            else {
                if(debug) { sb.append(properKey.getName()).append("\t").append(properKey.getTeam()).append("\t").append(properKey.getPosition()).append("\t");}

                Player earliest = gameweek.getStartingScores().get(properKey);
                Player latest = gameweek.getLatestScores().get(properKey);

                String earliestScore = earliest == null ? "0" : earliest.getScore();
                String latestScore = latest == null ? "0" : latest.getScore();

                int e = Integer.valueOf(earliestScore);
                int l = Integer.valueOf(latestScore);

                int score = l - e;

                if(score != 0 || latest.isPlaying()) {
                    sb.append(score).append("\n<br/>");
                } else {
                    sb.append("-9999\n<br/>");
                }
            }

        }

        return sb.toString();

    }

    private PlayerKey findProperKey(PlayerKey key) {

        if(gameweek.getLatestScores().containsKey(key)) {
            return key;
        }
        else {
            String [] split = key.getName().split(" ");
            PlayerKey found = null;
            int index = split.length - 1;

            do {
                String name = "";

                int i = index;
                while(i < split.length) {
                    name = name + " " + split[i];
                    i++;
                }

                index--;

                PlayerKey possible = new PlayerKey(name.trim(), key.getTeam(), key.getPosition());
                found = gameweek.getLatestScores().containsKey(possible) ? possible : null;

            } while(index > 0 && found == null);

            return found;
        }

    }


}
