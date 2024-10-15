package com.looigi.wallpaperchanger2.classeGps;

import android.content.Context;

import com.looigi.wallpaperchanger2.classeStandard.LogInterno;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class UtilityGPS {
    private static final String NomeMaschera = "UTILITY";

    private static UtilityGPS instance = null;

    private UtilityGPS() {
    }

    public static UtilityGPS getInstance() {
        if (instance == null) {
            instance = new UtilityGPS();
        }

        return instance;
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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("GPS", Maschera,  Log);
            // }
        } else {

        }
    }
}
