package com.looigi.wallpaperchanger2.classiStandard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

public class ScreenReceiver extends BroadcastReceiver {
    private boolean screenOff;
    private static String NomeMaschera = "SCREENRECEVIER";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
           screenOff = false;
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            screenOff = true;
        }
        VariabiliStaticheServizio.getInstance().setScreenOn(screenOff);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Cambio schermo: " + screenOff);
    }
}