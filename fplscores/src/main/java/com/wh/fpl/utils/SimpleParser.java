package com.wh.fpl.utils;

/**
 * Created by jkaye on 29/08/17.
 */
public class SimpleParser {

    private String remaining;

    private String lastValue;

    private Integer start;

    public SimpleParser(String remaining) {
        this.remaining = remaining;
    }

    public SimpleParser open(String match) {

        if(remaining != null) {
            start = remaining.indexOf(match);
            if (start > 0) {
                start += match.length(); //TODO - No Match

            }

        }
        lastValue = null;
        return this;
    }

    public String close(String match) {

        if(remaining != null) {
            int finish = match == null ? remaining.length() - 1 : remaining.indexOf(match);

            //If there is a start index
            if(start == null) {
                lastValue = finish < 0
                        ? remaining.substring(0)
                        : remaining.substring(0, finish);
            }
            else if (start >= 0) {
                lastValue = finish < 0
                        ? remaining.substring(start)
                        : remaining.substring(start, finish);
            } else if (start < 0) {
                lastValue = null;
                remaining = null;
                return lastValue;
            }

            remaining = match == null
                    ? remaining.substring(finish)
                    : remaining.substring(finish + match.length());
            start = null;
        }

        return lastValue;

    }

}
