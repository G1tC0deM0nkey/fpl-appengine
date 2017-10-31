package com.wh.fpl.core;

import java.util.*;

/**
 * Created by jkaye on 18/09/17.
 */
public class TeamScore {

    private Teamsheet teamsheet;

    private Formation formation;

    private Map<PlayerKey, Integer> playerScoresMap;

    private List <String> substitutions;

    public TeamScore(Teamsheet teamsheet) {
        this.teamsheet = teamsheet;
        this.formation = Formation.getFormation(teamsheet.getStarters());
        this.playerScoresMap = new HashMap<PlayerKey, Integer>();
        this.substitutions = new ArrayList<String>();
    }

    public void score(Gameweek gameweek, Style style) {

        for(PlayerKey p : teamsheet.getStarters()) {
                scorePlayer(gameweek, p);
        }

        if(style == Style.SUBSTITUTIONS) {
            List <PlayerKey> subs = new ArrayList<PlayerKey>(teamsheet.getSubs());

            for(PlayerKey p : teamsheet.getStarters()) {
                subAndScorePlayer(gameweek, p, subs);
            }
        }

    }

    private void subAndScorePlayer(Gameweek gameweek, PlayerKey p, List <PlayerKey> subs){

        if(gameweek == null || gameweek.getStartingScores() == null || gameweek.getLatestScores() == null) {
            playerScoresMap.put(p, 0);
            return;
        }

        if(gameweek.getStartingScores().get(p) == null || gameweek.getLatestScores().get(p) == null) {
            playerScoresMap.put(p, 0);
            return;
        }

        if(gameweek.getLatestScores().get(p).isPlaying()) {
            scorePlayer(gameweek, p);
        }
        else {
            if(subs == null || subs.size() == 0) {
                scorePlayer(gameweek, p);
            }
            else {
                PlayerKey substitute = null;

                for(PlayerKey sub : subs) {
                    boolean played = gameweek.getLatestScores().containsKey(sub) ?
                            gameweek.getLatestScores().get(sub).isPlaying() : false;
                    if(played) {
                        List <PlayerKey> teamWithSub = new ArrayList <PlayerKey> (playerScoresMap.keySet());
                        teamWithSub.remove(p);
                        teamWithSub.add(sub);

                        if(Formation.validFormation(teamWithSub)) {
                            substitute = sub;
                            playerScoresMap.remove(p);
                            subs.remove(sub);
                            break;
                        }
                    }
                }

                if(substitute != null) {
                    scorePlayer(gameweek, substitute);
                    substitutions.add("Sub " + substitute.getName() + " in for " + p.getName());
                }
                else {
                    scorePlayer(gameweek, p);
                }
            }
        }
    }

    private void scorePlayer(Gameweek gameweek, PlayerKey p) {

        if(gameweek == null || gameweek.getStartingScores() == null || gameweek.getLatestScores() == null) {
            playerScoresMap.put(p, 0);
            return;
        }

        if(gameweek.getStartingScores().get(p) == null || gameweek.getLatestScores().get(p) == null) {
            playerScoresMap.put(p, 0);
            return;
        }

        int start = Integer.parseInt(gameweek.getStartingScores().get(p).getScore());
        int end = Integer.parseInt(gameweek.getLatestScores().get(p).getScore());
        int score = end - start;

        if (p.equals(teamsheet.getCaptain())) {
            score *=2;
        }
        else if(p.equals(teamsheet.getVice())) {
            Player captain = gameweek.getLatestScores().get(teamsheet.getCaptain());
            if(captain == null || !captain.isPlaying()) {
                score *=2;
            }
        }

        playerScoresMap.put(p, score);
    }

    public PlayerKey getCaptain() {
        return teamsheet.getCaptain();
    }

    public PlayerKey getViceCaptain() {
        return teamsheet.getVice();
    }

    public List <String> getSubstitutions() {
        return substitutions;
    }

    public Integer getTotalScore() {
        int total = 0;

        for(PlayerKey p : playerScoresMap.keySet()) {
            total += playerScoresMap.get(p);
        }

        return total;
    }

    public List<PlayerKey> getPlayers() {
        ArrayList <PlayerKey> players = new ArrayList<PlayerKey>(playerScoresMap.keySet());
        Collections.sort(players, new PlayerPositionComparator());
        return players;
    }

    public Integer getScore(PlayerKey player) {
        return playerScoresMap.containsKey(player) ? playerScoresMap.get(player) : 0;
    }

    public enum Style {
        STANDARD,
        SUBSTITUTIONS,
        BEST_XI
    }
}
