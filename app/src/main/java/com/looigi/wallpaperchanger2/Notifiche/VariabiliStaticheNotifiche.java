package com.looigi.wallpaperchanger2.Notifiche;

public class VariabiliStaticheNotifiche {
    private static VariabiliStaticheNotifiche instance = null;

    private VariabiliStaticheNotifiche() {
    }

    public static VariabiliStaticheNotifiche getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheNotifiche();
        }

        return instance;
    }

}
