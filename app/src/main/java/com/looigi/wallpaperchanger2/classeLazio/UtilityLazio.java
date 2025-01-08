package com.looigi.wallpaperchanger2.classeLazio;

public class UtilityLazio {
    private static UtilityLazio instance = null;

    private UtilityLazio() {
    }

    public static UtilityLazio getInstance() {
        if (instance == null) {
            instance = new UtilityLazio();
        }

        return instance;
    }
}
