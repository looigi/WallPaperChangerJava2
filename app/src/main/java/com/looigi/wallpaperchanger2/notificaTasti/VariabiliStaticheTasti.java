package com.looigi.wallpaperchanger2.notificaTasti;

public class VariabiliStaticheTasti {
    private static VariabiliStaticheTasti instance = null;

    private VariabiliStaticheTasti() {
    }

    public static VariabiliStaticheTasti getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheTasti();
        }

        return instance;
    }

    private int idNotifica = 111119;
    public static String channelName = "WallPaperChangerII_Tasti";
    public static String NOTIFICATION_CHANNEL_STRING = "com.looigi.wallpaperchanger2";
    public static int NOTIFICATION_CHANNEL_ID = 7;

    public int getIdNotifica() {
        return idNotifica;
    }
}