package com.looigi.wallpaperchanger2.classeGps;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.log.LogInterno;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class UtilityGPS {
    private static final String NomeMaschera = "Utility_GPS";

    private static UtilityGPS instance = null;

    private UtilityGPS() {
    }

    public static UtilityGPS getInstance() {
        if (instance == null) {
            instance = new UtilityGPS();
        }

        return instance;
    }

    public void ImpostaAttesa(boolean come) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (VariabiliStaticheGPS.getInstance().getImgAttesa() != null) {
                    if (come) {
                        VariabiliStaticheGPS.getInstance().getImgAttesa().setVisibility(LinearLayout.VISIBLE);
                    } else {
                        VariabiliStaticheGPS.getInstance().getImgAttesa().setVisibility(LinearLayout.GONE);
                    }
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Gps", Maschera,  Log);
            // }
        } else {

        }
    }
}
