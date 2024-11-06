package com.looigi.wallpaperchanger2.notifiche;

import com.looigi.wallpaperchanger2.notificaTasti.VariabiliStaticheTasti;

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
