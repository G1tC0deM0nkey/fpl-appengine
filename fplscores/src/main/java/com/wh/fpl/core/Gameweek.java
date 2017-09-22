package com.wh.fpl.core;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by jkaye on 18/09/17.
 */
public class Gameweek {

    private int gameMonth;

    private int gameWeek;

    private Map<PlayerKey, Player> startingScores;

    private Map<PlayerKey, Player> latestScores;

    public Gameweek(int gameMonth, int gameWeek) {
        this.gameMonth = gameMonth;
        this.gameWeek = gameWeek;
        this.startingScores = new HashMap<PlayerKey, Player>();
        this.latestScores = new HashMap<PlayerKey, Player>();
    }

    public int getGameMonth() {
        return gameMonth;
    }

    public int getGameWeek() {
        return gameWeek;
    }

    public Map<PlayerKey, Player> getStartingScores() {
        return startingScores;
    }

    public Map<PlayerKey, Player> getLatestScores() {
        return latestScores;
    }

    public int getPlayerScore(PlayerKey key) {

        Player pStart = startingScores.get(key);
        Player pEnd = latestScores.get(key);

        if(pStart == null || pEnd == null) {
            return 0;
        }
        else {
            return Integer.parseInt(pEnd.getScore()) - Integer.parseInt(pStart.getScore());
        }

    }

    public void update(String ... tokens) {

        PlayerKey pk = new PlayerKey(tokens[2], tokens[0], tokens[1]);

        Player pStarting = getStartingScores().containsKey(pk) ? getStartingScores().get(pk) : new Player(pk);
        Player pLatest = getLatestScores().containsKey(pk) ? getStartingScores().get(pk) : new Player(pk);

        pStarting.setValue(tokens[3]);
        pLatest.setValue(tokens[3]);
        pStarting.setScore(tokens[4]);
        pLatest.setScore(tokens[6]);
        pLatest.setPlayed(Boolean.parseBoolean(tokens[5]));

        getStartingScores().put(pk, pStarting);
        getLatestScores().put(pk, pLatest);

    }

    public void export(PrintWriter pw) {

        Set <PlayerKey> playerKeys = new HashSet<PlayerKey>(startingScores.keySet());
        playerKeys.addAll(latestScores.keySet());

        List<PlayerKey> playerKeyList = new ArrayList<PlayerKey>(playerKeys);
        Collections.sort(playerKeyList, new Comparator<PlayerKey>() {
            public int compare(PlayerKey o1, PlayerKey o2) {

                int result = o1.getTeam().compareTo(o2.getTeam());

                if(result == 0) {
                    result = PlayerTeamPositionComparator.comparePositions(o1.getPosition(), o2.getPosition());
                }

                if(result == 0) {
                    result = o1.getName().compareTo(o2.getName());
                }

                return result;
            }
        });

        for(PlayerKey p : playerKeyList) {

            Player start = startingScores.get(p);
            Player end = latestScores.get(p);

            StringBuilder sb = new StringBuilder();

            sb.append(p.getTeam()).append("\t").append(p.getPosition()).
                    append("\t").append(p.getName()).append("\t");

            String value = start == null ? "£?" : start.getValue();
            int startingScore = start == null ? 0 : Integer.parseInt(start.getScore());
            int latestScore = end == null ? startingScore : Integer.parseInt(end.getScore());
            boolean playing = end == null ? false : end.isPlaying();
            int currentScore = latestScore - startingScore;

            sb.append(String.format("%s\t%s\t%s\t%s\t%s", value, startingScore, playing, latestScore, currentScore));

            pw.println(sb.toString());

        }

        pw.flush();

    }

    public void print(PrintWriter pw) {

        Set <PlayerKey> playerKeys = new HashSet<PlayerKey>(startingScores.keySet());
        playerKeys.addAll(latestScores.keySet());

        List<PlayerKey> playerKeyList = new ArrayList<PlayerKey>(playerKeys);
        Collections.sort(playerKeyList, new PlayerTeamPositionComparator());

        for(PlayerKey p : playerKeyList) {

            Player start = startingScores.get(p);
            Player end = latestScores.get(p);

            StringBuilder sb = new StringBuilder();

            sb.append(p.getTeam()).append("\t").append(p.getPosition()).
                    append("\t").append(p.getName()).append("\t");

            String value = start == null ? "£?" : start.getValue();
            int startingScore = start == null ? 0 : Integer.parseInt(start.getScore());
            int latestScore = end == null ? startingScore : Integer.parseInt(end.getScore());
            boolean playing = end == null ? false : end.isPlaying();
            int currentScore = latestScore - startingScore;

            sb.append(String.format("%s\t%s\t%s\t%s\t%s<br/>", value, startingScore, playing, latestScore, currentScore));

            pw.println(sb.toString());

        }

        pw.flush();

    }

}
