package com.wh.fpl.core;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jkaye on 22/09/17.
 */
public class TeamsheetParser {

    private String file;

    public TeamsheetParser(String file) {
        this.file = file;
    }

    public Map<String, Teamsheet> parse() throws IOException {

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        ArrayList <String> lines = new ArrayList<String>();

        while(line != null) {
            lines.add(line);
            line = br.readLine();
        }

        br.close();

        return parseTeamsheets(lines, 0);

    }

    private Map<String, Teamsheet> parseTeamsheets(List<String> lines, int index) {

        Map<String, Teamsheet> teamsheets = new HashMap<String, Teamsheet>();

        //Parse Teamsheet
        int current = index;
        int next = current + 7;

        String theTeam = lines.get(current);
        String lineTeam = lines.get(current);

        Teamsheet teamsheet = new Teamsheet();

        while(theTeam.equals(lineTeam)) {

            String player = lines.get(current + 3);
            String playerTeam = lines.get(current + 4);
            String playerPosition = lines.get(current + 5);

            String score = lines.get(current + 6);
            boolean captain = false;
            boolean vice = false;

            if ("C".equals(score)) {
                next = current + 8;
                captain = true;
            } else if ("VC".equals(score)) {
                next = current + 8;
                vice = true;
            }

            PlayerKey playerKey = new PlayerKey(player, playerTeam, playerPosition);

            if(teamsheet.getStarters().size() >= 11) {
                teamsheet.getSubs().add(playerKey);
            }
            else {
                teamsheet.getStarters().add(playerKey);
            }

            if(captain) {
                teamsheet.setCaptain(playerKey);
            } else if(vice) {
                teamsheet.setVice(playerKey);
            }

            current = next;
            next = current + 7;

            lineTeam = current < lines.size() ? lines.get(current) : null;

        }

        teamsheets.put(theTeam, teamsheet);

        if((current + 7) < lines.size()) {
            Logger.getLogger(getClass()).info("Current " + current + " max " + lines.size());
            teamsheets.putAll(parseTeamsheets(lines, current));
        }

        return teamsheets;

    }
}
