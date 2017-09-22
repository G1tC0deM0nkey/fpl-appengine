package com.wh.fpl.core;

import java.util.Comparator;

/**
 * Created by jkaye on 22/09/17.
 */
public class PlayerPositionComparator implements Comparator<PlayerKey> {

    @Override
    public int compare(PlayerKey o1, PlayerKey o2) {

        int result = comparePositions(o1.getPosition(), o2.getPosition());

        if(result == 0) {
            result = o1.getName().compareTo(o2.getName());
        }

        return result;
    }


    public static int comparePositions(String pos1, String pos2) {

        if(pos1.equals(pos2)) {
            return 0;
        }
        else {
            int p1;
            int p2;

            switch(pos1.charAt(0)) {
                case 'G': p1=1; break;
                case 'D': p1=2; break;
                case 'M': p1=3; break;
                case 'F': p1=4; break;
                default: p1=0; break;
            }

            switch(pos2.charAt(0)) {
                case 'G': p2=1; break;
                case 'D': p2=2; break;
                case 'M': p2=3; break;
                case 'F': p2=4; break;
                default: p2=0; break;
            }
            return p1 - p2;
        }

    }
}
