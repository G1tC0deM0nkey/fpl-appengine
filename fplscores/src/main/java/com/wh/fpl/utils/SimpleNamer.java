package com.wh.fpl.utils;

/**
 * Created by jkaye on 22/09/17.
 */
public class SimpleNamer {

    public static String simpleName(String name) {
        String newName = name.replace(" ", "");
        newName = newName.toLowerCase();
        return newName;
    }

}
